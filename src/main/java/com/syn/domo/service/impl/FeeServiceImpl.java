package com.syn.domo.service.impl;

import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FeeService;
import com.syn.domo.service.UserService;
import com.syn.domo.web.filter.FeeFilter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;

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

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository,
                          BuildingService buildingService,
                          ApartmentService apartmentService,
                          UserService userService,
                          NotificationService notificationService,
                          ModelMapper modelMapper) {
        this.feeRepository = feeRepository;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<FeeServiceModel> getAll(FeeFilter feeFilter, Pageable pageable) {

        Set<FeeServiceModel> fees = this.feeRepository
                .findAll(feeFilter, pageable).getContent().stream()
                .map(f -> this.modelMapper.map(f, FeeServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(fees);
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
        fee.setPaidDate(LocalDateTime.now());
        fee.setPaid(true);
        fee.setPayerId(userId);

        try {
            this.notificationService.sendFeePaymentReceipt(userServiceModel, fee);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        this.feeRepository.saveAndFlush(fee);

        this.buildingService.addToBudget(fee.getTotal(),
                fee.getApartment().getBuilding().getId());

        return this.modelMapper.map(fee, FeeServiceModel.class);
    }

    @Override
    public int deleteAll(FeeFilter feeFilter) {

        List<Fee> fees = this.feeRepository.findAll(feeFilter);

        this.feeRepository.deleteAll(fees);

        return fees.size();
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
    public void generateMonthlyFees() throws MessagingException {

        Set<ApartmentServiceModel> apartments = this.apartmentService.getAll();

        for (ApartmentServiceModel apartment : apartments) {
            Fee fee = new Fee();
            fee.setIssueDate(LocalDate.now());
            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setApartment(this.modelMapper.map(apartment, Apartment.class));

            BigDecimal total = this.calculateFeeTotal(apartment,
                    apartment.getBuilding().getBaseFee());

            fee.setTotal(total);

            this.feeRepository.saveAndFlush(fee);

            for (UserServiceModel resident : apartment.getResidents()) {
                this.notificationService.sendNewFeeNotificationEmail(resident, fee);
            }
        }

        log.info("******* FEES GENERATED *******");
    }

    private BigDecimal calculateFeeTotal(ApartmentServiceModel apartment, BigDecimal baseFee) {
        BigDecimal total = new BigDecimal("0");
        int inhabitants = apartment.getResidents().size() +
                apartment.getChildren().size() +
                apartment.getPets();

        if (inhabitants == 0) {
            total = total.add(baseFee);
        } else {
            total = total.add(baseFee.multiply(BigDecimal.valueOf(inhabitants)));
            if (apartment.getFloor() > 2) {
                total = total.add(baseFee);
            }
        }

        return total;
    }

}
