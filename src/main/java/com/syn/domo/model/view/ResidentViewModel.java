package com.syn.domo.model.view;

import java.util.Set;

public class ResidentViewModel extends UserViewModel {

    private Set<ApartmentsInResidentViewModel> apartments;
    private Set<ChildViewModel> children;

    public ResidentViewModel() {
    }

    public Set<ApartmentsInResidentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentsInResidentViewModel> apartments) {
        this.apartments = apartments;
    }

    public Set<ChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildViewModel> children) {
        this.children = children;
    }

}
