package com.syn.domo.model.service;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntityServiceModel)) return false;
        if (!super.equals(o)) return false;
        UserEntityServiceModel that = (UserEntityServiceModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, userRole);
    }
}
