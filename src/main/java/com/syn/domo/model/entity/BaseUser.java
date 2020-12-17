package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class BaseUser extends BaseEntity {

    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String identityCardNumber;
    private String phoneNumber;
    private UserRole userRole;
    private LocalDate addedOn;
    private LocalDate removedOn;

    public BaseUser() {
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "id_card_number", unique = true)
    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String idCardNumber) {
        this.identityCardNumber = idCardNumber;
    }

    @Column(name = "phone_number", unique = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String personalNumber) {
        this.phoneNumber = personalNumber;
    }

    @Enumerated(EnumType.STRING)
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Column(name = "removed_on")
    public LocalDate getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(LocalDate removedOn) {
        this.removedOn = removedOn;
    }
}
