package com.syn.domo.model.service;

import java.util.Set;

public class ResidentServiceModel extends UserEntityServiceModel {

    private ApartmentServiceModel apartment;
    private Set<ChildServiceModel> children;

    public ResidentServiceModel() {
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
    }

    public Set<ChildServiceModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildServiceModel> children) {
        this.children = children;
    }
}
