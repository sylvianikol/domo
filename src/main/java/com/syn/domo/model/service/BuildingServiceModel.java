package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Set;

public class BuildingServiceModel extends BaseServiceModel {

    private String name;
    private String address;
    private int floorsNumber;
    private LocalDate addedOn;
    private LocalDate archivedOn;

    Set<FloorServiceModel> floors;
    Set<ApartmentServiceModel> apartments;

    public BuildingServiceModel() {
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

    public int getFloorsNumber() {
        return floorsNumber;
    }

    public void setFloorsNumber(int floorsNumber) {
        this.floorsNumber = floorsNumber;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public LocalDate getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(LocalDate archivedOn) {
        this.archivedOn = archivedOn;
    }

    public Set<FloorServiceModel> getFloors() {
        return floors;
    }

    public void setFloors(Set<FloorServiceModel> floors) {
        this.floors = floors;
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
    }
}
