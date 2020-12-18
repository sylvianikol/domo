package com.syn.domo.model.binding;

public class ChildAddBindingModel extends BaseUserEntityAddBindingModel {

    private String apartment;

    public ChildAddBindingModel() {
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
