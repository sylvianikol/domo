package com.syn.domo.model.view;

public abstract class UserEntityViewModel extends BaseUserEntityViewModel {

    private String email;
    private String identityCardNumber;
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

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
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
    public String toString() {
        return String.format("%s, email: %s, ID Card: %s, phone: %s",
                super.toString(),
                this.getEmail(),
                this.getIdentityCardNumber(),
                this.getPhoneNumber());
    }


}
