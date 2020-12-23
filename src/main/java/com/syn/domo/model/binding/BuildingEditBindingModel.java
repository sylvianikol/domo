package com.syn.domo.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class BuildingEditBindingModel {

    private String name;
    private String neighbourhood;
    private String address;
    private LocalDate addedOn;
    private LocalDate archivedOn;

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
}
