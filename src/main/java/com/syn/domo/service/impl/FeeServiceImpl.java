package com.syn.domo.service.impl;

import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FeeService;
import com.syn.domo.service.UserService;
import com.syn.domo.utils.UrlCheckerUtil;
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

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.DefaultParamValues.*;
import static com.syn.domo.common.ExceptionErrorMessages.*;
import static com.syn.domo.model.entity.Fee.BASE_FEE;

@Service
public class FeeServiceImpl implements FeeService {

    private static final Logger log =
            LoggerFactory.getLogger(FeeServiceImpl.class);

    private final FeeRepository feeRepository;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final UrlCheckerUtil urlCheckerUtil;

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository,
                          BuildingService buildingService,
                          ApartmentService apartmentService,
                          UserService userService,
                          NotificationService notificationService,
                          ModelMapper modelMapper,
                          UrlCheckerUtil urlCheckerUtil) {
        this.feeRepository = feeRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.urlCheckerUtil = urlCheckerUtil;
    }

    @Override
    public Map<String, Object> getAll(String buildingId, String apartmentId,
                                      int page, int size, String[] sort) {

        List<Order> orders = this.createOrders(sort);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Fee> pageFees;

        if (this.urlCheckerUtil.areEmpty(buildingId, apartmentId)) {
            pageFees = this.feeRepository.findAllBy(pagingSort);
        } else if (this.urlCheckerUtil.areEmpty(apartmentId)){

            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            pageFees = this.feeRepository
                    .getAllByBuildingIdWithPagingSort(buildingId, pagingSort);

        } else {
            if (!this.urlCheckerUtil.areEmpty(buildingId) && this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            if (this.apartmentService.get(apartmentId).isEmpty()) {
                throw new EntityNotFoundException(APARTMENT_NOT_FOUND);
            }

            pageFees = this.feeRepository.findAllByApartmentId(apartmentId, pagingSort);
        }

        List<FeeViewModel> fees = pageFees.getContent().stream()
                .map(fee -> this.modelMapper.map(fee, FeeViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        Map<String, Object> response = new HashMap<>();

        if (fees.isEmpty()) {
            response.put(FEES_RESPONSE_KEY, null);
        } else {
            response.put(FEES_RESPONSE_KEY, fees);
            response.put(CURRENT_PAGE, pageFees.getNumber());
            response.put(TOTAL_ITEMS, pageFees.getTotalElements());
            response.put(TOTAL_PAGES, pageFees.getTotalPages());
        }

        return response;
    }

    @Override
    public Optional<FeeServiceModel> get(String feeId) {

        Optional<Fee> fee = this.feeRepository.findById(feeId);

        return fee.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(fee.get(), FeeServiceModel.class));
    }

    @Override
    public FeeServiceModel pay(String userId, String feeId) throws MessagingException {

        Fee fee = this.feeRepository.findById(feeId).orElse(null);

        if (fee == null) {
            throw new EntityNotFoundException(FEE_NOT_FOUND);
        }

        if (fee.isPaid()) {
            throw new UnprocessableEntityException(FEE_ALREADY_PAID);
        }

        UserServiceModel userServiceModel = this.userService.get(userId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(USER_NOT_FOUND);
                });

        // TODO: make a mock payment
        fee.setPaidOn(LocalDateTime.now());
        fee.setPaid(true);

        this.notificationService.sendFeePaymentReceipt(userServiceModel, fee);
        this.feeRepository.saveAndFlush(fee);

        this.buildingService
                .addToBudget(fee.getTotal(), fee.getApartment().getBuilding().getId());
        // TODO: Add income to Building's budget
        return this.modelMapper.map(fee, FeeServiceModel.class);
    }

    @Override
    public void delete(String feeId) {

        Fee fee = this.feeRepository.findById(feeId).orElse(null);

        if (fee == null) {
            throw new EntityNotFoundException(FEE_NOT_FOUND);
        }

        this.feeRepository.delete(fee);
    }

    @Override
    public void deleteAll(String buildingId, String apartmentId) {

        Set<Fee> fees;

        if (this.urlCheckerUtil.areEmpty(buildingId, apartmentId)) {
            fees = new HashSet<>(this.feeRepository.findAll());
        } else if (this.urlCheckerUtil.areEmpty(apartmentId)){

            if (this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            fees = this.feeRepository.getAllByBuildingId(buildingId);

        } else {
            if (!this.urlCheckerUtil.areEmpty(buildingId)
                    && this.buildingService.get(buildingId).isEmpty()) {
                throw new EntityNotFoundException(BUILDING_NOT_FOUND);
            }

            if (this.apartmentService.get(apartmentId).isEmpty()) {
                throw new EntityNotFoundException(APARTMENT_NOT_FOUND);
            }

            fees = this.feeRepository.findAllByApartmentId(apartmentId);
        }

        this.feeRepository.deleteAll(fees);
    }

    @Override
    public void generateMonthlyFees() throws MessagingException {

        Set<ApartmentServiceModel> apartments = this.apartmentService.getAll(EMPTY_VALUE);

        for (ApartmentServiceModel apartment : apartments) {
            Fee fee = new Fee();
            fee.setIssueDate(LocalDate.now());
            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setApartment(this.modelMapper.map(apartment, Apartment.class));

            BigDecimal total = this.calculateFeeTotal(apartment);

            fee.setTotal(total);

            this.feeRepository.saveAndFlush(fee);

            for (UserServiceModel resident : apartment.getResidents()) {
                this.notificationService.sendNewFeeNotificationEmail(resident, fee);
            }
        }

        log.info("******* FEES GENERATED *******");
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
