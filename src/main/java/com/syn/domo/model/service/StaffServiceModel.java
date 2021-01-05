package com.syn.domo.model.service;

import java.util.Set;

public class StaffServiceModel extends UserServiceModel {

    private Set<BuildingServiceModel> buildings;

    public StaffServiceModel() {
    }

    public Set<BuildingServiceModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingServiceModel> buildings) {
        this.buildings = buildings;
    }
}
