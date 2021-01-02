package com.syn.domo.model.view;

import java.time.LocalDate;
import java.util.Objects;

public abstract class BaseUserViewModel {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate addedOn;

    public BaseUserViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(o instanceof BaseUserViewModel)) return false;
        BaseUserViewModel that = (BaseUserViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, addedOn);
    }
}
