package com.syn.domo.model.service;

public class BuildingServiceModel {

    private Long id;
    private String address;
    private int floorsNumber;
    private int apartmentsPerFloor;

    public BuildingServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloorsNumber() {
        return floorsNumber;
    }

    public void setFloorsNumber(int floorsNumber) {
        this.floorsNumber = floorsNumber;
    }

    public int getApartmentsPerFloor() {
        return apartmentsPerFloor;
    }

    public void setApartmentsPerFloor(int apartmentsPerFloor) {
        this.apartmentsPerFloor = apartmentsPerFloor;
    }
}
