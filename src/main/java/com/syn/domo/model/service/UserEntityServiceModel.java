package com.syn.domo.model.service;

public abstract class UserEntityServiceModel extends BaseUserEntityServiceModel {

    //    private String password;
    private String email;
    private String phoneNumber;
    private String userRole;

    public UserEntityServiceModel() {
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
