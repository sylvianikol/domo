package com.syn.domo.model.view;

import java.util.Set;

public class ResidentViewModel extends UserViewModel {

    private Set<ApartmentsInnerViewModel> apartments;
    private Set<ChildViewModel> children;

    public ResidentViewModel() {
    }

    public Set<ApartmentsInnerViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentsInnerViewModel> apartments) {
        this.apartments = apartments;
    }

    public Set<ChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildViewModel> children) {
        this.children = children;
    }

}
