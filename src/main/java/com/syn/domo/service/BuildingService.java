package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.BuildingViewModel;

import java.util.Set;

public interface BuildingService {

    Building getById(String id);

    BuildingServiceModel constructBuilding(BuildingConstructModel buildingConstructModel);

    void saveBuilding(Building building);

    boolean hasBuildings();

    Set<BuildingServiceModel> getAllBuildings();

    int getCount();
}
