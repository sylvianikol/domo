package com.syn.domo.model.service;

public class ChildServiceModel extends BaseUserEntityServiceModel {

    private ApartmentServiceModel apartment;

    public ChildServiceModel() {
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
    }
}
