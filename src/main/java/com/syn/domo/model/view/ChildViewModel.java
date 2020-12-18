package com.syn.domo.model.view;

public class ChildViewModel extends BaseUserEntityViewModel {

    private String apartment;

    public ChildViewModel() {
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
