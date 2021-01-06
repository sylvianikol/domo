package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Optional;
import java.util.Set;

public interface BuildingService {

    Set<BuildingServiceModel> getAll();

    Optional<BuildingServiceModel> getOne(String buildingName, String buildingAddress, String neighbourhood);

    Optional<BuildingServiceModel> getById(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    void delete(String buildingId);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);

    void releaseStaff(String staffId);

    Set<BuildingServiceModel> getAllByIdIn(Set<String> ids);

}
