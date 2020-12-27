package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    ResidentServiceModel add(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId);

    ResidentServiceModel edit(ResidentServiceModel residentServiceModel, String buildingId, String apartmentId);

    Set<ResidentServiceModel> getAllByApartmentId(String apartmentId);

    Optional<ResidentServiceModel> getById(String residentId);

    void deleteAllByApartmentId(String apartmentId);

    void delete(String residentId, String buildingId, String apartmentId);
}
