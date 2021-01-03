package com.syn.domo.model.view;

import java.util.Objects;
import java.util.Set;

public class UserViewModel extends BaseUserViewModel {

    private String email;
    private String phoneNumber;
    private Set<RoleViewModel> roles;

    public UserViewModel() {
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

    public Set<RoleViewModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleViewModel> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserViewModel)) return false;
        if (!super.equals(o)) return false;
        UserViewModel that = (UserViewModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, roles);
    }
}
