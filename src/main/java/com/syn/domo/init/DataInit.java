package com.syn.domo.init;

import com.syn.domo.service.JobService;
import com.syn.domo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final JobService jobService;
    private final StaffService staffService;

    @Autowired
    public DataInit(JobService jobService, StaffService staffService) {
        this.jobService = jobService;
        this.staffService = staffService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.jobService.initJobRoles();
        this.staffService.initStaff();
    }
}
