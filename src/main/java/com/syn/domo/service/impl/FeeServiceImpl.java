package com.syn.domo.service.impl;

import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository,
                          ApartmentService apartmentService,
                          ModelMapper modelMapper) {
        this.feeRepository = feeRepository;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }


    @Override
//    @Scheduled(cron = "0 0 10 1 * ?")
    public void generateMonthlyFees() {
        System.out.println("generateMonthlyFees() triggered!");
    }
}
