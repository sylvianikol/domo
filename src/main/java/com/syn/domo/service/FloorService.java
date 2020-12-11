package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.model.view.BuildingViewModel;

import java.util.List;

public interface FloorService {

    FloorServiceModel getByNumber(int number);

    BuildingViewModel constructBuilding(BuildingConstructModel buildingConstructModel);

    BuildingViewModel getBuildingDetails();

    boolean isBuilt();

    List<Integer> getAllFloorNumbers();

    boolean hasCapacity(int floorNumber);

    boolean isBuildingFull();
}
