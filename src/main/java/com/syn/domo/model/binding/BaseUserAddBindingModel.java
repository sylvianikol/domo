package com.syn.domo.model.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_INVALID;

public abstract class BaseUserAddBindingModel {

//    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String idCardNumber;
    private String phoneNumber;
    private String userRole;
    private LocalDate addedOn;
    private LocalDate removedOn;

    public BaseUserAddBindingModel() {
    }

//    @NotNull(message = PASSWORD_NULL)
//    @NotEmpty(message = PASSWORD_EMPTY)
//    @Length(min = 3, max = 30, message = PASSWORD_INVALID)
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    @NotNull(message = EMAIL_NULL)
    @NotEmpty(message = EMAIL_EMPTY)
    @Email(message = EMAIL_INVALID)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
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

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public LocalDate getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(LocalDate removedOn) {
        this.removedOn = removedOn;
    }
}
