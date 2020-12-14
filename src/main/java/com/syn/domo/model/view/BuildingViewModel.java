package com.syn.domo.model.view;

public class BuildingViewModel {

    private String name;
    private String address;
    private int floors;

    public BuildingViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return String.format("%s, located at %s",
                this.getName(), this.getAddress());
    }
}
