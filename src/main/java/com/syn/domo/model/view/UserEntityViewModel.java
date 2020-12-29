package com.syn.domo.model.view;

import java.util.Objects;

public abstract class UserEntityViewModel extends BaseUserEntityViewModel {

    private String email;
    private String phoneNumber;
    private String userRole;

    public UserEntityViewModel() {
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
        if (!(o instanceof UserEntityViewModel)) return false;
        if (!super.equals(o)) return false;
        UserEntityViewModel that = (UserEntityViewModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, userRole);
    }
}
