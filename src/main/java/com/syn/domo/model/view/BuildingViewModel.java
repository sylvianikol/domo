package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Set;

public class BuildingViewModel {

    private String id;
    private String name;
    private String address;
    private LocalDate addedOn;
    private LocalDate archivedOn;
    private Set<FloorViewModel> floors;

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

    public Set<FloorViewModel> getFloors() {
        return floors;
    }

    public void setFloors(Set<FloorViewModel> floors) {
        this.floors = floors;
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

    public Set<ApartmentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentViewModel> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return String.format("%s (\"%s\") - Floors: %d, Managed apartments: %d.",
                this.getName(), this.getAddress(), this.getFloors().size(), this.getApartments().size());
    }
}
