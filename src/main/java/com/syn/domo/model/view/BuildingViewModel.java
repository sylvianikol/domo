package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class BuildingViewModel {

    private String id;
    private String name;
    private String neighbourhood;
    private String address;
    private int floors;
    private LocalDate addedOn;

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

    public Set<ApartmentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentViewModel> apartments) {
        this.apartments = apartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingViewModel)) return false;
        BuildingViewModel that = (BuildingViewModel) o;
        return floors == that.floors &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(neighbourhood, that.neighbourhood) &&
                Objects.equals(address, that.address) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, neighbourhood, address, floors, addedOn);
    }
}
