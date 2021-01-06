package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Optional;
import java.util.Set;

public interface BuildingService {

    Set<BuildingServiceModel> getAll();

    Optional<BuildingServiceModel> getOne
            (String buildingName, String buildingAddress, String neighbourhood);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);

    void delete(String buildingId);

    void assignStaff(String buildingId, Set<String> staffIds);

    void releaseStaff(String staffId);

    Optional<BuildingServiceModel> getById(String id);

    Set<BuildingServiceModel> getAllByIdIn(Set<String> ids);
}
