package com.syn.domo.model.binding;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.syn.domo.common.RegexPatterns.APARTMENT_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class ApartmentEditBindingModel {

    private String id;
    private String number;
    private int floor;
    private int pets;
    private LocalDate addedOn;

    public ApartmentEditBindingModel() {
    }

    @NotNull(message = ID_NULL)
    @NotEmpty(message = ID_EMPTY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = APARTMENT_NUMBER_NULL)
    @NotEmpty(message = APARTMENT_NUMBER_EMPTY)
    @Pattern(regexp = APARTMENT_NUMBER_REGEX, message = APARTMENT_INVALID_SYMBOLS)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NotNull(message = FLOOR_NULL)
    @Min(value = 0, message = FLOOR_MIN_INVALID)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
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

    @NotNull(message = DATE_NULL)
    @NotEmpty(message = DATE_EMPTY)
    @PastOrPresent(message = DATE_INVALID)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }
}
