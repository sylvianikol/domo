package com.syn.domo.service.impl;

import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FeeService;
import com.syn.domo.scheduled.ScheduledFeesGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.model.entity.Fee.BASE_FEE;

@Service
public class FeeServiceImpl implements FeeService {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledFeesGenerator.class);

    private final FeeRepository feeRepository;
    private final BuildingService buildingService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository,
                          BuildingService buildingService,
                          NotificationService notificationService,
                          ModelMapper modelMapper) {
        this.feeRepository = feeRepository;
        this.buildingService = buildingService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Map<String, Object> getAllByBuilding(String buildingId,
                                                int page, int size, String[] sort) {

        List<Order> orders = this.createOrders(sort);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        Page<Fee> pageFees = this.feeRepository
                .getAllByBuildingIdWithPagingSort(buildingId, pagingSort);

        List<FeeViewModel> fees = pageFees.getContent().stream()
                .map(fee -> this.modelMapper.map(fee, FeeViewModel.class))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();

        if (fees.isEmpty()) {
            response.put("fees", null);
        } else {
            response.put("fees", fees);
            response.put("currentPage", pageFees.getNumber());
            response.put("totalItems", pageFees.getTotalElements());
            response.put("totalPages", pageFees.getTotalPages());
        }

        return response;
    }

    @Override
    public Map<String, Object> getAll(int page, int size, String[] sort) {
        List<Order> orders = this.createOrders(sort);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Fee> pageFees = this.feeRepository
                .findAllBy(pagingSort);

        List<FeeViewModel> fees = pageFees.getContent().stream()
                .map(fee -> this.modelMapper.map(fee, FeeViewModel.class))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();

        if (fees.isEmpty()) {
            response.put("fees", null);
        } else {
            response.put("fees", fees);
            response.put("currentPage", pageFees.getNumber());
            response.put("totalItems", pageFees.getTotalElements());
            response.put("totalPages", pageFees.getTotalPages() - 1);
        }

        return response;
    }

    @Override
    public Optional<FeeServiceModel> getOne(String feeId, String buildingId) {

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Optional<Fee> fee = this.feeRepository
                .getOneByIdAndBuildingId(feeId, buildingId);

        return fee.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(fee.get(), FeeServiceModel.class));
    }

    @Override
    public FeeServiceModel pay(String feeId, String buildingId) {
        //TODO: validation
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Fee fee = this.feeRepository.findById(feeId).orElse(null);

        if (fee == null) {
            throw new EntityNotFoundException("Fee not found!");
        }

        if (fee.isPaid()) {
            throw new UnprocessableEntityException("Fee is already paid!");
        }

        fee.setPaid(true);
        this.feeRepository.saveAndFlush(fee);

        return this.modelMapper.map(fee, FeeServiceModel.class);
    }

    @Override
    public void delete(String feeId, String buildingId) {
        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Fee fee = this.feeRepository.findById(feeId).orElse(null);

        if (fee == null) {
            throw new EntityNotFoundException("Fee not found!");
        }

        this.feeRepository.delete(fee);
    }

    @Override
    public void deleteAll(String buildingId) {

        Optional<BuildingServiceModel> building = this.buildingService.getById(buildingId);
        if (building.isEmpty()) {
            throw new BuildingNotFoundException("Building not found!");
        }

        Set<Fee> fees = this.feeRepository.getAllByBuildingId(buildingId);
        this.feeRepository.deleteAll(fees);
    }

    @Override
    public void generateMonthlyFees() {
        Set<BuildingServiceModel> buildingServiceModels = this.buildingService.getAll();

        for (BuildingServiceModel building : buildingServiceModels) {
            Set<ApartmentServiceModel> apartments = building.getApartments();
            for (ApartmentServiceModel apartment : apartments) {
                Fee fee = new Fee();
                fee.setIssueDate(LocalDate.now());
                fee.setDueDate(LocalDate.now().plusMonths(1));
                fee.setApartment(this.modelMapper.map(apartment, Apartment.class));

                BigDecimal total = this.calculateFeeTotal(apartment);

                fee.setTotal(total);

                this.feeRepository.saveAndFlush(fee);

                for (UserServiceModel resident : apartment.getResidents()) {
                    this.notificationService.sendEmail(resident);
                }
            }
        }
    }

    private BigDecimal calculateFeeTotal(ApartmentServiceModel apartment) {
        BigDecimal total = new BigDecimal("0");
        int inhabitants = apartment.getResidents().size() +
                apartment.getChildren().size() +
                apartment.getPets();

        if (inhabitants == 0) {
            total = total.add(BASE_FEE);
        } else {
            total = total.add(BASE_FEE.multiply(BigDecimal.valueOf(inhabitants)));
            if (apartment.getFloor() > 2) {
                total = total.add(BASE_FEE);
            }
        }

        return total;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    private List<Order> createOrders(String[] sort) {
        List<Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(this.getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

}
