package com.syn.domo.model.view;

import java.util.Set;

public class ResidentViewModel extends UserViewModel {

    private Set<ApartmentViewModel> apartments;
    private Set<ChildViewModel> children;

    public ResidentViewModel() {
    }

    public Set<ApartmentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentViewModel> apartments) {
        this.apartments = apartments;
    }

    public Set<ChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildViewModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResidentViewModel)) return false;
        return super.equals(o);
    }
}
