package com.syn.domo.model.service;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

import static com.syn.domo.common.ValidationErrorMessages.*;

public abstract class BaseUserServiceModel extends BaseServiceModel {

    private String firstName;
    private String lastName;
    private LocalDate addedOn;

    public BaseUserServiceModel() {
    }

    public BaseUserServiceModel(String id, String firstName, String lastName, LocalDate addedOn) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.addedOn = addedOn;
    }

    @NotNull(message = FIRST_NAME_NOT_NULL)
    @NotEmpty(message = FIRST_NAME_NOT_EMPTY)
    @Size(min = 2, max = 55, message = FIRST_NAME_INVALID)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = LAST_NAME_NOT_NULL)
    @NotEmpty(message = LAST_NAME_NOT_EMPTY)
    @Size(min = 2, max = 55, message = LAST_NAME_INVALID)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Future(message = DATE_FUTURE)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUserServiceModel)) return false;
        if (!super.equals(o)) return false;
        BaseUserServiceModel that = (BaseUserServiceModel) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, addedOn);
    }
}
