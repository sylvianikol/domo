package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Objects;

public abstract class BaseUserEntityServiceModel extends BaseServiceModel {

    private String firstName;
    private String lastName;
    private LocalDate addedOn;

    public BaseUserEntityServiceModel() {
    }

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

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUserEntityServiceModel)) return false;
        if (!super.equals(o)) return false;
        BaseUserEntityServiceModel that = (BaseUserEntityServiceModel) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, addedOn);
    }
}
