package com.syn.domo.service;

import com.syn.domo.model.service.FloorServiceModel;

import java.util.Set;

public interface FloorService {

    FloorServiceModel getByNumberAndBuildingId(int number, String buildingId);

    Set<FloorServiceModel> createFloors(int floorsNumber, String buildingId);

    Set<Integer> getAllFloorNumbersByBuildingId(String buildingId);

    void archiveAllByBuildingId(String buildingId);
}
