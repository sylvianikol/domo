package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Set;

public class ResidentViewModel extends UserViewModel {

    @JsonManagedReference
    private Set<ApartmentViewModel> apartments;
    @JsonBackReference
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

}
