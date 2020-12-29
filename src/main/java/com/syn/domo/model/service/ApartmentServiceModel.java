package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class ApartmentServiceModel extends BaseServiceModel {

    private String number;
    private int floor;
    private BuildingServiceModel building;
    private int pets;
    private LocalDate addedOn;

    private Set<ResidentServiceModel> residents;
    private Set<ChildServiceModel> children;

    public ApartmentServiceModel() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public BuildingServiceModel getBuilding() {
        return building;
    }

    public void setBuilding(BuildingServiceModel building) {
        this.building = building;
    }

    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public Set<ResidentServiceModel> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentServiceModel> residents) {
        this.residents = residents;
    }

    public Set<ChildServiceModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildServiceModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApartmentServiceModel)) return false;
        if (!super.equals(o)) return false;
        ApartmentServiceModel that = (ApartmentServiceModel) o;
        return floor == that.floor &&
                pets == that.pets &&
                Objects.equals(number, that.number) &&
                Objects.equals(building.getId(), that.building.getId()) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, floor, building, pets, addedOn);
    }
}
