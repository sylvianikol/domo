package com.syn.domo.model.binding;

public class ChildAddBindingModel extends BaseUserEntityBindingModel {

    private String apartmentId;

    public ChildAddBindingModel() {
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }
}
