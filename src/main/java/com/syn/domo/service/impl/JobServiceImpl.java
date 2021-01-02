package com.syn.domo.service.impl;

import com.syn.domo.model.entity.JobPosition;
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
            job.setPosition(JobPosition.SUPER.toString());
            job.setWage(BigDecimal.valueOf(0));
            this.jobRepository.saveAndFlush(job);
        }
    }

    @Override
    public JobServiceModel findByJobPosition(String jobRole) {
        Job job = this.jobRepository.findByPosition(jobRole).orElse(null);

        if (job == null) {
            return null;
        }

        return this.modelMapper.map(job, JobServiceModel.class);
    }

    @Override
    public List<String> getAllJobRoles() {
        return this.jobRepository.findAll().stream()
                .map(Job::getPosition)
                .collect(Collectors.toList());
    }
}
