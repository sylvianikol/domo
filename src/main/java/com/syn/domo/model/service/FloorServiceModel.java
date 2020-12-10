package com.syn.domo.model.service;

public class FloorServiceModel extends BaseServiceModel {

    private int number;
    private int apartmentsPerFloor;

    public FloorServiceModel() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getApartmentsPerFloor() {
        return apartmentsPerFloor;
    }

    public void setApartmentsPerFloor(int apartmentsPerFloor) {
        this.apartmentsPerFloor = apartmentsPerFloor;
    }
}
