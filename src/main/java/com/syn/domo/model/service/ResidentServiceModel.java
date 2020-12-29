package com.syn.domo.model.service;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResidentServiceModel)) return false;
        if (!super.equals(o)) return false;
        ResidentServiceModel that = (ResidentServiceModel) o;
        return Objects.equals(apartment.getId(), that.apartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartment);
    }
}
