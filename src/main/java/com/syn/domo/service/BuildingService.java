package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Set;

public interface BuildingService {

    BuildingServiceModel getById(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    boolean hasBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();

    String getBuildingName(String id);

    BuildingServiceModel archive(String buildingId);

    boolean alreadyExists(String buildingName, String buildingAddress);

    boolean isArchived(String buildingName, String buildingAddress);

    BuildingServiceModel getByNameAndAddress(String buildingName, String buildingAddress);
}
