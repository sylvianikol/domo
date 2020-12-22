package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Set;

public class ApartmentServiceModel extends BaseServiceModel {

    private String number;
    private int floorNumber;
    private BuildingServiceModel building;
    private int pets;
    private LocalDate addedOn;
    private LocalDate archivedOn;

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

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
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

    public LocalDate getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(LocalDate archivedOn) {
        this.archivedOn = archivedOn;
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
}
