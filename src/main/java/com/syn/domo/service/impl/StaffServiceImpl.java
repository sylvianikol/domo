package com.syn.domo.service.impl;

import com.syn.domo.model.entity.InitJobRoles;
import com.syn.domo.model.entity.Job;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.JobServiceModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.JobService;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final JobService jobService;
    private final ModelMapper modelMapper;


    public StaffServiceImpl(StaffRepository staffRepository, JobService jobService, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.jobService = jobService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initStaff() {
        if (this.staffRepository.count() == 0) {
            Staff admin = new Staff();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@domo.bg");
            admin.setPassword("123");
            admin.setPhoneNumber("0888147384573");
            admin.setAddedOn(LocalDate.now());
            admin.setUserRole(UserRole.ADMIN);

            JobServiceModel jobServiceModel =
                     this.jobService.findByJobRole(InitJobRoles.SUPER.toString());

            if (jobServiceModel != null) {
                admin.setJob(this.modelMapper.map(jobServiceModel, Job.class));
                this.staffRepository.saveAndFlush(admin);
            }
        }
    }
}
