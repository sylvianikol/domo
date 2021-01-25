package com.syn.domo.model.service;

import java.time.LocalDate;
import java.util.Set;

public class ResidentServiceModel extends UserServiceModel  {

    private Set<ApartmentServiceModel> apartments;
    private Set<ChildServiceModel> children;

    public ResidentServiceModel() {
    }

    public ResidentServiceModel(String id, String firstName, String lastName, LocalDate addedOn, String email, String phoneNumber, boolean isActive, Set<RoleServiceModel> roles, Set<ApartmentServiceModel> apartments, Set<ChildServiceModel> children) {
        super(id, firstName, lastName, addedOn, email, phoneNumber, isActive, roles);
        this.apartments = apartments;
        this.children = children;
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
}
