package com.syn.domo.service;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Floor;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.service.FloorServiceModel;
import com.syn.domo.model.view.BuildingViewModel;

import java.util.List;
import java.util.Set;

public interface FloorService {

    FloorServiceModel getByNumber(int number);

    void createFloors(int floorsNumber, Long buildingId);

    Set<Floor> getAllByBuildingId(Long id);

    boolean hasFloors();

    List<Integer> getAllFloorNumbers();

    int countFloors();
}
