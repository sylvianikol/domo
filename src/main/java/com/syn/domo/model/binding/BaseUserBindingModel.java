package com.syn.domo.model.binding;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.syn.domo.common.ValidationErrorMessages.*;

@MappedSuperclass
public abstract class BaseUserBindingModel {

    private String firstName;
    private String lastName;

    public BaseUserBindingModel() {
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

    @NotNull(message = LAST_NAME_NULL)
    @NotEmpty(message = LAST_NAME_EMPTY)
    @Size(min = 2, max = 55, message = LAST_NAME_INVALID)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
