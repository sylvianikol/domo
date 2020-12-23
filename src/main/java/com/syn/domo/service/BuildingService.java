package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Optional;
import java.util.Set;

public interface BuildingService {

    BuildingServiceModel getById(String id);

    Optional<BuildingServiceModel> getOptById(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    boolean hasActiveBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();

    String getBuildingName(String id);

    BuildingServiceModel archive(String buildingId);

    Optional<BuildingServiceModel> getBuilding(String buildingName, String buildingAddress, String neighbourhood);

    BuildingServiceModel activate(String buildingId);

    BuildingServiceModel delete(String buildingId);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);
}
