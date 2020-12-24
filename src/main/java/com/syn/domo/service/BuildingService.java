package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Optional;
import java.util.Set;

public interface BuildingService {

    Set<BuildingServiceModel> getAllBuildings();

    Optional<BuildingServiceModel> getBuilding(String buildingName, String buildingAddress, String neighbourhood);

    BuildingServiceModel getById(String id);

    Optional<BuildingServiceModel> getOptById(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    BuildingServiceModel delete(String buildingId);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);

    // TODO: delete all below
    boolean hasActiveBuildings();

    int getCount();

    String getBuildingName(String id);
}
