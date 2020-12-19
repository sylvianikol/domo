package com.syn.domo.model.view;

public class ChildViewModel extends BaseUserEntityViewModel {

    private String apartmentNumber;

    public ChildViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }
}
