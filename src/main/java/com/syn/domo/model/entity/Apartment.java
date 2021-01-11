package com.syn.domo.model.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
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

    @NotNull(message = APARTMENT_NUMBER_NULL)
    @NotEmpty(message = APARTMENT_NUMBER_EMPTY)
    @Size(min = 1, max = 10, message = APARTMENT_LENGTH_INVALID)
    @Pattern(regexp = APARTMENT_NUMBER_REGEX, message = APARTMENT_INVALID_SYMBOLS)
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NotNull(message = FLOOR_NUMBER_NULL)
    @Min(value = 0, message = FLOOR_MIN_INVALID)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    @Column(nullable = false)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @NotNull(message = BUILDING_NULL)
    @ManyToOne
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @NotNull(message = PETS_NULL)
    @Min(value = 0, message = PETS_MIN)
    @Max(value = 5, message = PETS_MAX)
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

    @NotNull(message = DATE_NULL)
    @Future(message = DATE_INVALID)
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
}
