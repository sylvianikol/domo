package com.syn.domo.model.service;

public class ResidentServiceModel extends UserEntityServiceModel {

    private String apartment;

    public ResidentServiceModel() {
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
