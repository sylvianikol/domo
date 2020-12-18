package com.syn.domo.model.binding;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseUserEntityAddBindingModel {

    private String firstName;
    private String lastName;

    public BaseUserEntityAddBindingModel() {
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
}
