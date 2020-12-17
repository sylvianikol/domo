package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;

import java.util.Set;

public interface ResidentService {

    ResidentServiceModel register(ResidentServiceModel residentServiceModel, String apartment);

    Set<ResidentServiceModel> getAllResidents();

    Set<ResidentServiceModel> getAllResidentsByApartmentId(String apartmentId);
}
