package com.syn.domo.model.service;

import java.util.Objects;
import java.util.Set;

public class UserServiceModel extends BaseUserServiceModel {

    //    private String password;
    private String email;
    private String phoneNumber;
    private Set<RoleServiceModel> roles;
    private ApartmentServiceModel apartment;
    private Set<ChildServiceModel> children;

    public UserServiceModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<RoleServiceModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleServiceModel> roles) {
        this.roles = roles;
    }

    public ApartmentServiceModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentServiceModel apartment) {
        this.apartment = apartment;
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
        if (!(o instanceof UserServiceModel)) return false;
        if (!super.equals(o)) return false;
        UserServiceModel that = (UserServiceModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, apartment);
    }
}
