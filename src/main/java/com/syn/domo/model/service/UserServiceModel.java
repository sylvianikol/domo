package com.syn.domo.model.service;

import java.util.Set;

public class UserServiceModel extends BaseUserServiceModel {

    //    private String password;
    private String email;
    private String phoneNumber;
    private Set<RoleServiceModel> roles;

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
}
