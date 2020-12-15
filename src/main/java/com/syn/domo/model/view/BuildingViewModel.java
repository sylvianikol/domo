package com.syn.domo.model.view;

public class BuildingViewModel {

    private String id;
    private String name;
    private String address;
    private int floors;

    public BuildingViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return String.format("%s with %d floors, located at %s.",
                this.getName(), this.getFloors(), this.getAddress());
    }
}
