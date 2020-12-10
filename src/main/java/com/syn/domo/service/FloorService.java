package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.model.view.BuildingViewModel;

public interface FloorService {

    FloorServiceModel getByNumber(int number);

    BuildingViewModel constructBuilding(BuildingConstructModel buildingConstructModel);

    BuildingViewModel getBuildingDetails();

    boolean isBuilt();
}
