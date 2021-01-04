package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    ResidentServiceModel add(UserServiceModel userServiceModel, String buildingId, String apartmentId);

    ResidentServiceModel edit(UserServiceModel userServiceModel, String buildingId, String apartmentId);

    void delete(String buildingId, String apartmentId, String residentId);

    Set<ResidentServiceModel> getAllByBuildingIdAndApartmentId(String buildingId, String apartmentId);

    Optional<ResidentServiceModel> getOne(String buildingId, String apartmentId, String residentId);

    Set<ResidentServiceModel> getAllByIdIn(Set<String> ids);
}
