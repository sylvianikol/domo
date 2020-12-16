package com.syn.domo.model.view;

import java.util.Set;

public class BuildingViewModel {

    private String id;
    private String name;
    private String address;
    private int floors;

    private Set<ApartmentViewModel> apartments;

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

    public Set<ApartmentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentViewModel> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return String.format("%s (\"%s\") - Floors: %d, Managed apartments: %d.",
                this.getName(), this.getAddress(), this.getFloors(), this.getApartments().size());
    }
}
