package com.syn.domo.service.impl;

import com.syn.domo.model.entity.*;
import com.syn.domo.model.service.JobServiceModel;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.JobService;
import com.syn.domo.service.RoleService;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleService roleService;
    private final JobService jobService;
    private final ModelMapper modelMapper;


    public StaffServiceImpl(StaffRepository staffRepository, RoleService roleService, JobService jobService, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.roleService = roleService;
        this.jobService = jobService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void initStaff() {
        if (this.staffRepository.count() == 0) {
            Staff admin = new Staff();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@domo.bg");
            admin.setPassword("123");
            admin.setPhoneNumber("0888147384573");
            admin.setAddedOn(LocalDate.now());

            Set<Role> roles = this.roleService.getAll();
            roles.forEach(role -> role.getUsers().add(admin));
            admin.setRoles(roles);

            JobServiceModel jobServiceModel =
                     this.jobService.findByJobPosition(JobPosition.SUPER.toString());

            if (jobServiceModel != null) {
                admin.setJob(this.modelMapper.map(jobServiceModel, Job.class));
                this.staffRepository.saveAndFlush(admin);

            }
        }
    }
}
