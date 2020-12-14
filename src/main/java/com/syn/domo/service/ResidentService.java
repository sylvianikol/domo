package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;

import java.util.Set;

public interface ResidentService {

    ResidentServiceModel register(ResidentServiceModel residentServiceModel);

    Set<ResidentServiceModel> getAllResidents();
}
