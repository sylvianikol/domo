package com.syn.domo.model.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static com.syn.domo.common.RegexPatterns.APARTMENT_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    private String number;
    private int floor;
    private Building building;
    private int pets;
    private LocalDate addedOn;

    private Set<Resident> residents;
    private Set<Child> children;
    private Set<Fee> fees;

    public Apartment() {
    }

    public Apartment(String number, int floor, Building building, int pets, LocalDate addedOn) {
        this.number = number;
        this.floor = floor;
        this.building = building;
        this.pets = pets;
        this.addedOn = addedOn;
    }

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
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

    @ManyToMany(mappedBy = "apartments", fetch = EAGER)
    public Set<Resident> getResidents() {
        return residents;
    }

    public void setResidents(Set<Resident> residents) {
        this.residents = residents;
    }

    @OneToMany(mappedBy = "apartment", fetch = EAGER)
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

    @OneToMany(mappedBy = "apartment", cascade = REMOVE, fetch = EAGER)
    public Set<Fee> getFees() {
        return fees;
    }

    public void setFees(Set<Fee> fees) {
        this.fees = fees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apartment)) return false;
        if (!super.equals(o)) return false;
        Apartment apartment = (Apartment) o;
        return floor == apartment.floor &&
                pets == apartment.pets &&
                Objects.equals(number, apartment.number) &&
                Objects.equals(building, apartment.building) &&
                Objects.equals(addedOn, apartment.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, floor, building, pets, addedOn);
    }
}
