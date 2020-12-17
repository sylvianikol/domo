package com.syn.domo.model.binding;

public class ResidentAddBindingModel extends BaseUserAddBindingModel {

    private String apartment;

    public ResidentAddBindingModel() {
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
