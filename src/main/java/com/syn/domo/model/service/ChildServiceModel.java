package com.syn.domo.model.service;

import java.util.Objects;
import java.util.Set;

public class ChildServiceModel extends BaseUserServiceModel {

    private Set<ApartmentServiceModel> apartments;
    private Set<ResidentServiceModel> parents;

    public ChildServiceModel() {
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
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
