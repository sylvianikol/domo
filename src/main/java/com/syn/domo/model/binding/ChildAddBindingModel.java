package com.syn.domo.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class ChildAddBindingModel {

    private String firstName;
    private String lastName;
    private String apartmentId;

    public ChildAddBindingModel() {
    }

    @NotNull(message = FIRST_NAME_NULL)
    @NotEmpty(message = FIRST_NAME_EMPTY)
    @Size(min = 2, max = 55, message = FIRST_NAME_INVALID)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }
}
