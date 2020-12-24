package com.syn.domo.service;

import com.syn.domo.model.service.FloorServiceModel;

import java.util.Optional;
import java.util.Set;

public interface FloorService {

    Optional<FloorServiceModel> getByNumberAndBuildingId(int number, String buildingId);

    boolean alreadyExists(int number, String buildingId);

    Set<FloorServiceModel> createFloors(int floorsNumber, String buildingId);

    void deleteAllByBuildingId(String buildingId);

    Set<Integer> getAllFloorNumbersByBuildingId(String buildingId);

    void archiveAllByBuildingId(String buildingId);
}
