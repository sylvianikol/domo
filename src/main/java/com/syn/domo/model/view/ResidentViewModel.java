package com.syn.domo.model.view;

public class ResidentViewModel extends BaseUserViewModel {

    private String apartmentNumber;

    public ResidentViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

}