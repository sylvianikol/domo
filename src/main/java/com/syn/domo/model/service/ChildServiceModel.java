package com.syn.domo.model.service;

public class ChildServiceModel extends BaseUserEntityServiceModel {

    private String apartment;

    public ChildServiceModel() {
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
