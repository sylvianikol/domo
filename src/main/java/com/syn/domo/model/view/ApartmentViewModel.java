package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Set;

public class ApartmentViewModel {

    private String id;
    private String number;
    private int floor;
    private int pets;
    private LocalDate addedOn;

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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
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

    public Set<ResidentViewModel> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentViewModel> residents) {
        this.residents = residents;
    }
}
