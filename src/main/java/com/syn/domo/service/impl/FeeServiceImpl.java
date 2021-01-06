package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.notification.service.NotificationService;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FeeService;
import com.syn.domo.task.ScheduledFeesGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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
    public void generateMonthlyFees() {
        Set<BuildingServiceModel> buildingServiceModels = this.buildingService.getAll();

        for (BuildingServiceModel building : buildingServiceModels) {
            Set<ApartmentServiceModel> apartments = building.getApartments();
            for (ApartmentServiceModel apartment : apartments) {
                Fee fee = new Fee();
                fee.setIssueDate(LocalDate.now());
                fee.setDueDate(LocalDate.now().plusMonths(1));
                fee.setApartment(this.modelMapper.map(apartment, Apartment.class));

                BigDecimal total = this.calculateTotal(apartment);

                fee.setTotal(total);

                this.feeRepository.saveAndFlush(fee);

                for (UserServiceModel resident : apartment.getResidents()) {
                    this.notificationService.sendNotification(resident);
                }
            }
        }

        log.info("++++++++++++++++   FEES GENERATED!   ++++++++++++++++++++");
    }

    private BigDecimal calculateTotal(ApartmentServiceModel apartment) {
        BigDecimal total = new BigDecimal("0");
        int inhabitants = apartment.getResidents().size() +
                apartment.getChildren().size() +
                apartment.getPets();

        total = total.add(BASE_FEE.multiply(BigDecimal.valueOf(inhabitants)));
        if (apartment.getFloor() > 2) {
            total = total.add(BASE_FEE);
        }

        return total;
    }
}
