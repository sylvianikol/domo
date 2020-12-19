package com.syn.domo.model.view;

public class ResidentViewModel extends UserEntityViewModel {

    private String apartmentNumber;

    public ResidentViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        return String.format("%s, apartment No: %s", super.toString(), this.getApartmentNumber());
    }
}
