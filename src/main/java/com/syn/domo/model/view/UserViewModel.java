package com.syn.domo.model.view;

import java.util.Objects;
import java.util.Set;

public class UserViewModel extends BaseUserViewModel {

    private String email;
    private String phoneNumber;
    private String userRole;
    private String apartmentNumber;
    private Set<ChildViewModel> children;

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Set<ChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildViewModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserViewModel)) return false;
        if (!super.equals(o)) return false;
        UserViewModel that = (UserViewModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(userRole, that.userRole) &&
                Objects.equals(apartmentNumber, that.apartmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, userRole, apartmentNumber);
    }
}
