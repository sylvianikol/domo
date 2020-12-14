package com.syn.domo.model.service;

public class FloorServiceModel extends BaseServiceModel {

    private int number;
    private BuildingServiceModel building;

    public FloorServiceModel() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BuildingServiceModel getBuilding() {
        return building;
    }

    public void setBuilding(BuildingServiceModel building) {
        this.building = building;
    }
}
