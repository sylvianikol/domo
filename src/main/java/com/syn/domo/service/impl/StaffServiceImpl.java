package com.syn.domo.service.impl;

import com.syn.domo.model.entity.JobRole;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.repository.StaffRepository;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;


    public StaffServiceImpl(StaffRepository staffRepository, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    private void init() {

        if (this.staffRepository.count() == 0) {
            Staff admin = new Staff();
            admin.setEmail("admin@domo.bg");
            admin.setPassword("123");
            admin.setRole(UserRole.ADMIN);
            admin.setJobType(JobRole.SUPER);

            this.staffRepository.saveAndFlush(admin);
        }
    }

}
