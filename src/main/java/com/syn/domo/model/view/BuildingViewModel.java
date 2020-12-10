package com.syn.domo.model.view;

public class BuildingViewModel {

    private int floors;
    private int totalApartments;

    public BuildingViewModel() {
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getTotalApartments() {
        return totalApartments;
    }

    public void setTotalApartments(int totalApartments) {
        this.totalApartments = totalApartments;
    }
}
