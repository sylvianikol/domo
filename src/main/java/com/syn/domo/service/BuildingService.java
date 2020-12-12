package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.view.BuildingViewModel;

public interface BuildingService {

    boolean isBuilt();

    Building getById(Long id);

    BuildingViewModel getBuildingDetails(Long id);

    BuildingViewModel constructBuilding(BuildingConstructModel buildingConstructModel);

    void saveBuilding(Building building);
}
