package com.syn.domo.model.view;

import java.time.LocalDate;

public abstract class BaseUserEntityViewModel {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate addedOn;
    private LocalDate removedOn;

    public BaseUserEntityViewModel() {
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

    public LocalDate getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(LocalDate removedOn) {
        this.removedOn = removedOn;
    }

    @Override
    public String toString() {
        String removedOn = this.getRemovedOn() == null
                ? "n/a"
                : this.getRemovedOn().toString();

        return String.format("%s %s, added on: %s, removed on: %s",
                this.getFirstName(),
                this.getLastName(),
                this.getAddedOn(),
                removedOn);
    }
}
