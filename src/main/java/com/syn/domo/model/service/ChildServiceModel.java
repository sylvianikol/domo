package com.syn.domo.model.service;

import java.util.Objects;
import java.util.Set;

public class ChildServiceModel extends BaseUserEntityServiceModel {

    private ApartmentServiceModel apartment;
    private Set<ResidentServiceModel> parents;

    public ChildServiceModel() {
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
    }

    public Set<ResidentServiceModel> getParents() {
        return parents;
    }

    public void setParents(Set<ResidentServiceModel> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildServiceModel)) return false;
        if (!super.equals(o)) return false;
        ChildServiceModel that = (ChildServiceModel) o;
        return Objects.equals(apartment.getId(), that.apartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartment);
    }
}
