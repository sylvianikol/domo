package com.syn.domo.model.view;

public class BuildingViewModel {

    private String name;
    private String address;
    private int floors;
    private int addedApartments;

    public BuildingViewModel() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getAddedApartments() {
        return addedApartments;
    }

    public void setAddedApartments(int addedApartments) {
        this.addedApartments = addedApartments;
    }
}
