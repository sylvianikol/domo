package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Set;

public interface BuildingService {

    BuildingServiceModel getByName(String name);

    BuildingServiceModel addBuilding(BuildingAddBindingModel buildingAddBindingModel);

    void saveBuilding(BuildingServiceModel buildingServiceModel);

    boolean hasBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();
}
