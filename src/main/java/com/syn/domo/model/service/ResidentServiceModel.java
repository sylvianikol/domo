package com.syn.domo.model.service;

import java.util.Set;

public class ResidentServiceModel extends UserServiceModel  {

    private Set<ApartmentServiceModel> apartments;
    private Set<ChildServiceModel> children;

    public ResidentServiceModel() {
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
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
        return super.equals(o);
    }
}
