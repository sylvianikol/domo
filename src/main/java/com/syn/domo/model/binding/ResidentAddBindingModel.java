package com.syn.domo.model.binding;

public class ResidentAddBindingModel extends BaseUserAddBindingModel {

    private String apartmentNumber;

    public ResidentAddBindingModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
