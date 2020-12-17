package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Set;

public interface BuildingService {

    BuildingServiceModel getById(String id);

    BuildingServiceModel addBuilding(BuildingServiceModel buildingServiceModel);

    boolean hasBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();

    String getBuildingName(String id);
}
