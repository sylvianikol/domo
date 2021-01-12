package com.syn.domo.model.service;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class BuildingServiceModel extends BaseServiceModel {

    private String name;
    private String address;
    private String neighbourhood;
    private int floors;
    private LocalDate addedOn;

    Set<ApartmentServiceModel> apartments;

    public BuildingServiceModel() {
    }

    @NotNull(message = BUILDING_NAME_NULL)
    @NotEmpty(message = BUILDING_NAME_EMPTY)
    @Size(max = 40, message = BUILDING_NAME_INVALID_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = NEIGHBOURHOOD_NULL)
    @NotEmpty(message = NEIGHBOURHOOD_EMPTY)
    @Size(max = 40, message = NEIGHBOURHOOD_INVALID_LENGTH)
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @NotNull(message = ADDRESS_NULL)
    @NotEmpty(message = ADDRESS_EMPTY)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull(message = FLOOR_NUMBER_NULL)
    @Min(value = 0, message = FLOOR_MIN_INVALID)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    @Future(message = DATE_FUTURE)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingServiceModel)) return false;
        if (!super.equals(o)) return false;
        BuildingServiceModel that = (BuildingServiceModel) o;
        return floors == that.floors &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(neighbourhood, that.neighbourhood) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address, neighbourhood, floors, addedOn);
    }
}
