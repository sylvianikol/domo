package com.syn.domo.service;

import com.syn.domo.model.service.FloorServiceModel;

import java.util.List;

public interface FloorService {

    FloorServiceModel getByNumber(int number);

    void createFloors(int floorsNumber, String buildingId);

    List<Integer> getAllFloorNumbers();
}
