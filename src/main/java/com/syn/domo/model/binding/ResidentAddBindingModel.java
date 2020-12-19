package com.syn.domo.model.binding;

public class ResidentAddBindingModel extends UserEntityAddBindingModel {

    private String apartmentId;

    public ResidentAddBindingModel() {
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }
}
