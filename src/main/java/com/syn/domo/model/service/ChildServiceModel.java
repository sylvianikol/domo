package com.syn.domo.model.service;

import java.util.Objects;
import java.util.Set;

public class ChildServiceModel extends BaseUserServiceModel {

    private ApartmentServiceModel apartment;
    private Set<UserServiceModel> parents;

    public ChildServiceModel() {
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
    }

    public Set<UserServiceModel> getParents() {
        return parents;
    }

    public void setParents(Set<UserServiceModel> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildServiceModel)) return false;
        if (!super.equals(o)) return false;
        ChildServiceModel that = (ChildServiceModel) o;
        return Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartment);
    }
}
