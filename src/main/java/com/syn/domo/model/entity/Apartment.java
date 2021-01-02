package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    private String number;
    private int floor;
    private Building building;
    private int pets;
    private LocalDate addedOn;

    private Set<UserEntity> residents;
    private Set<Child> children;

    public Apartment() {
    }

    @Column(nullable = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(nullable = false)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @ManyToOne
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Column
    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    public Set<UserEntity> getResidents() {
        return residents;
    }

    public void setResidents(Set<UserEntity> residents) {
        this.residents = residents;
    }

    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apartment)) return false;
        Apartment apartment = (Apartment) o;
        return floor == apartment.floor &&
                pets == apartment.pets &&
                Objects.equals(number, apartment.number) &&
                Objects.equals(building.getId(), apartment.building.getId()) &&
                Objects.equals(addedOn, apartment.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, floor, building, pets, addedOn);
    }
}
