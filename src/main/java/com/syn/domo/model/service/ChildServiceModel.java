package com.syn.domo.model.service;

import java.util.Set;

public class ChildServiceModel extends BaseUserServiceModel {

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
        return super.equals(o);
    }
}
