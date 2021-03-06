package com.syn.domo.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static com.syn.domo.common.RegexPatterns.APARTMENT_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class ApartmentBindingModel {

    private String number;
    private int floor;
    private int pets;

    public ApartmentBindingModel() {
    }

    @NotNull(message = APARTMENT_NUMBER_NOT_NULL)
    @NotEmpty(message = APARTMENT_NUMBER_NOT_EMPTY)
    @Pattern(regexp = APARTMENT_NUMBER_REGEX, message = APARTMENT_INVALID_NUMBER)
    @Length(min = 1, max = 10, message = APARTMENT_LENGTH_INVALID)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NotNull(message = FLOOR_NUMBER_NOT_NULL)
    @Min(value = 0, message = FLOOR_MIN)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @NotNull(message = PETS_NOT_NULL)
    @Min(value = 0, message = PETS_MIN)
    @Max(value = 5, message = PETS_MAX)
    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }
}
