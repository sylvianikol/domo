package com.syn.domo.task;

import com.syn.domo.service.FeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledFeesGenerator {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledFeesGenerator.class);

    @Autowired
    private final FeeService feeService;

    public ScheduledFeesGenerator(FeeService feeService) {
        this.feeService = feeService;
    }

    //    @Scheduled(cron = "0 0 10 1 * ?")
    @Scheduled(fixedRate = 5000)
    public void generateMonthlyFees() {
        log.info("Fees generated!");
        this.feeService.generateMonthlyFees();
    }
}
