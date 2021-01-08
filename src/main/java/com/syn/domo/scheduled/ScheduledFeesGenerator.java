package com.syn.domo.scheduled;

import com.syn.domo.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledFeesGenerator {

    private final FeeService feeService;

    @Autowired
    public ScheduledFeesGenerator(FeeService feeService) {
        this.feeService = feeService;
    }

//    @Scheduled(cron = "0 0 10 1 * ?")
    @Scheduled(initialDelay = 5000, fixedDelay=Long.MAX_VALUE)
    public void generateMonthlyFees() {
        this.feeService.generateMonthlyFees();
    }
}
