package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.ResponseModel;

import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    ResponseModel<ResidentServiceModel> add(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId);

    ResponseModel<ResidentServiceModel> edit(ResidentServiceModel residentServiceModel,
                              String buildingId, String apartmentId, String residentId);

    void deleteAll(String buildingId, String apartmentId);

    void delete(String buildingId, String apartmentId, String residentId);

    Set<ResidentServiceModel> getAll(String buildingId, String apartmentId);

    Optional<ResidentServiceModel> get(String buildingId, String apartmentId, String residentId);

    Set<ResidentServiceModel> getAllByIdIn(Set<String> ids);

}
