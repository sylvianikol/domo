package com.syn.domo.scheduler;

import com.syn.domo.service.FeeService;
import com.syn.domo.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;


@Component
public class ScheduledPaymentsGenerator {

    private final FeeService    feeService;
    private final SalaryService salaryService;

    @Autowired
    public ScheduledPaymentsGenerator(FeeService feeService, SalaryService salaryService) {
        this.feeService = feeService;
        this.salaryService = salaryService;
    }

    @Scheduled(cron = "0 0 10 1 * ?") // generate fees on the 1st date each month
//    @Scheduled(initialDelay = 5000, fixedDelay=Long.MAX_VALUE) // for testing
    public void generateMonthlyFees() throws MessagingException, InterruptedException {
        this.feeService.generateMonthlyFees();
    }

    @Scheduled(cron = "0 0 10 5 * ?") // generate salaries on the 5th date each month
//    @Scheduled(initialDelay = 5000, fixedDelay=Long.MAX_VALUE) // for testing
    public void generateSalaries() throws MessagingException, InterruptedException {
        this.salaryService.generateSalaries();
    }
}
