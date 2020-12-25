package com.syn.domo.model.binding;

import javax.validation.constraints.*;

import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class BuildingEditBindingModel {

    private String name;
    private String neighbourhood;
    private String address;
    private int floors;
    private LocalDate addedOn;

    public BuildingEditBindingModel() {
    }

    @NotNull(message = BUILDING_ENTITY + NAME_NULL)
    @NotEmpty(message = BUILDING_ENTITY + NAME_EMPTY)
    @Size(max = 40, message = BUILDING_ENTITY + NAME_INVALID_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = BUILDING_ENTITY + NEIGHBOURHOOD_NULL)
    @NotEmpty(message = BUILDING_ENTITY + NEIGHBOURHOOD_EMPTY)
    @Size(max = 40, message = BUILDING_ENTITY + NEIGHBOURHOOD_INVALID_LENGTH)
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @NotNull(message = BUILDING_ENTITY + ADDRESS_NULL)
    @NotEmpty(message = BUILDING_ENTITY + ADDRESS_EMPTY)
    @Size(max = 40, message = BUILDING_ENTITY + ADDRESS_INVALID_LENGTH)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    @NotNull
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

}
