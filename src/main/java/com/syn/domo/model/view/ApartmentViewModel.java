package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Set;

public class ApartmentViewModel {

    private String id;
    private String number;
    private int floorNumber;
    private int pets;
    private LocalDate addedOn;
    private LocalDate archivedOn;

    Set<ResidentViewModel> residents;

    public ApartmentViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<ResidentViewModel> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentViewModel> residents) {
        this.residents = residents;
    }
}
