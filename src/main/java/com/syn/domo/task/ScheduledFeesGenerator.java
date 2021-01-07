package com.syn.domo.task;

import com.syn.domo.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledFeesGenerator {

    private final FeeService feeService;

    @Autowired
    public ScheduledFeesGenerator(FeeService feeService) {
        this.feeService = feeService;
    }

    @Scheduled(cron = "0 10 1 * ?")
//    @Scheduled(fixedRate = 5000)
    public void generateMonthlyFees() {
        this.feeService.generateMonthlyFees();
    }
}
