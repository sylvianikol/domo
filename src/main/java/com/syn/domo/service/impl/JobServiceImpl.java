package com.syn.domo.service.impl;

import com.syn.domo.model.entity.InitJobRoles;
import com.syn.domo.model.entity.Job;
import com.syn.domo.model.service.JobServiceModel;
import com.syn.domo.repository.JobRepository;
import com.syn.domo.service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, ModelMapper modelMapper) {
        this.jobRepository = jobRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initJobRoles() {
        if (this.jobRepository.count() == 0) {
            Job job = new Job();
            job.setRole(InitJobRoles.SUPER.toString());
            job.setWage(BigDecimal.valueOf(0));
            this.jobRepository.saveAndFlush(job);
        }
    }

    @Override
    public JobServiceModel findByJobRole(String jobRole) {
        Job job = this.jobRepository.findByRole(jobRole).orElse(null);

        if (job == null) {
            return null;
        }

        return this.modelMapper.map(job, JobServiceModel.class);
    }

    @Override
    public List<String> getAllJobRoles() {
        return this.jobRepository.findAll().stream()
                .map(Job::getRole)
                .collect(Collectors.toList());
    }
}
