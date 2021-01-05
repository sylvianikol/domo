package com.syn.domo.model.view;

import java.util.Set;

public class StaffViewModel extends UserViewModel {

    private Set<BuildingInnerViewModel> buildings;

    public StaffViewModel() {
    }

    public Set<BuildingInnerViewModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingInnerViewModel> buildings) {
        this.buildings = buildings;
    }
}
