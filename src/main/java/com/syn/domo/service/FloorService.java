package com.syn.domo.service;

import com.syn.domo.model.service.FloorServiceModel;

import java.util.List;
import java.util.Set;

public interface FloorService {

    FloorServiceModel getByNumber(int number);

    Set<FloorServiceModel> createFloors(int floorsNumber, String buildingId);

    List<Integer> getAllFloorNumbers();
}
