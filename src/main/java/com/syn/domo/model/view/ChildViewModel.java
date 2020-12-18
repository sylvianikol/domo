package com.syn.domo.model.view;

public class ChildViewModel {

    private String firstName;
    private String lastName;
    private String apartment;

    public ChildViewModel() {
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

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }
}
