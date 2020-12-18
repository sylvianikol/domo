package com.syn.domo.model.binding;

public class ResidentEntityAddBindingModel extends UserEntityAddBindingModel {

    private String apartment;

    public ResidentEntityAddBindingModel() {
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
