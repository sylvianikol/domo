package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;

import java.util.Set;

public interface ResidentService {

    ResidentServiceModel add(ResidentServiceModel residentServiceModel);

    Set<ResidentServiceModel> getAllResidents();

    Set<ResidentServiceModel> getAllResidentsByApartmentId(String apartmentId);

    ResidentServiceModel getById(String residentId);
}
