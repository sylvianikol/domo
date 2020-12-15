package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Set;

public interface BuildingService {

    Building getById(String id);

    BuildingServiceModel addBuilding(BuildingAddBindingModel buildingAddBindingModel);

    void saveBuilding(Building building);

    boolean hasBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();
}
