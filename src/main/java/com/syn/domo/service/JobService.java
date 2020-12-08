package com.syn.domo.service;

import com.syn.domo.model.service.JobServiceModel;

import java.util.List;

public interface JobService {

    void initJobRoles();

    JobServiceModel findByJobRole(String jobRole);

    List<String> getAllJobRoles();
}
