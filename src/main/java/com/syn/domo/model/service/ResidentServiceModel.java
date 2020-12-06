package com.syn.domo.model.service;

public class ResidentServiceModel extends BaseUserServiceModel {

    private String apartmentNumber;

    public ResidentServiceModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
