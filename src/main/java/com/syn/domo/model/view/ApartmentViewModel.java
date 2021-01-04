package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class ApartmentViewModel {

    private String id;
    private String number;
    private int floor;
    private int pets;
    private LocalDate addedOn;

    Set<ResidentInnerViewModel> residents;
    Set<ChildInnerViewModel> children;

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

    public Set<ResidentInnerViewModel> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentInnerViewModel> residents) {
        this.residents = residents;
    }

    public Set<ChildInnerViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildInnerViewModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApartmentViewModel)) return false;
        ApartmentViewModel that = (ApartmentViewModel) o;
        return floor == that.floor &&
                pets == that.pets &&
                Objects.equals(id, that.id) &&
                Objects.equals(number, that.number) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, floor, pets, addedOn);
    }
}
