package com.syn.domo.model.service;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static com.syn.domo.common.RegexPatterns.APARTMENT_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.APARTMENT_LENGTH_INVALID;

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

    @NotNull(message = APARTMENT_NUMBER_NULL)
    @NotEmpty(message = APARTMENT_NUMBER_EMPTY)
    @Pattern(regexp = APARTMENT_NUMBER_REGEX, message = APARTMENT_INVALID_SYMBOLS)
    @Length(min = 1, max = 10, message = APARTMENT_LENGTH_INVALID)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NotNull(message = FLOOR_NUMBER_NULL)
    @Min(value = 0, message = FLOOR_MIN_INVALID)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
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

    @NotNull(message = PETS_NULL)
    @Min(value = 0, message = PETS_MIN)
    @Max(value = 5, message = PETS_MAX)
    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    @Future(message = DATE_FUTURE)
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
                Objects.equals(building, that.building) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, floor, building, pets, addedOn);
    }

}
