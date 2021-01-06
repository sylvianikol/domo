package com.syn.domo.model.binding;

import java.util.Set;

public class StaffAssignBuildingsBindingModel {

    private Set<BuildingIdBindingModel> buildings;

    public StaffAssignBuildingsBindingModel() {
    }

    public Set<BuildingIdBindingModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingIdBindingModel> buildings) {
        this.buildings = buildings;
    }
}
