package com.syn.domo.model.service;

public class ResidentServiceModel extends UserEntityServiceModel {

    private ApartmentServiceModel apartment;

    public ResidentServiceModel() {
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
    }
}
