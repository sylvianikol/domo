package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class BuildingServiceModel extends BaseServiceModel {

    private String name;
    private String address;
    private String neighbourhood;
    private int floors;
    private LocalDate addedOn;

    Set<ApartmentServiceModel> apartments;

    public BuildingServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
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

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingServiceModel)) return false;
        if (!super.equals(o)) return false;
        BuildingServiceModel that = (BuildingServiceModel) o;
        return floors == that.floors &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(neighbourhood, that.neighbourhood) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address, neighbourhood, floors, addedOn);
    }
}
