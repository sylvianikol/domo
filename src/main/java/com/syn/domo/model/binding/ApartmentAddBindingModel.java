package com.syn.domo.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static com.syn.domo.common.RegexPatterns.APARTMENT_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class ApartmentAddBindingModel {

    private String number;
    private Integer floorNumber;
    // TODO: private String buildingName;

    public ApartmentAddBindingModel() {
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

    @NotNull(message = FLOOR_NULL)
    @Min(value = -3, message = FLOOR_MIN_INVALID)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
