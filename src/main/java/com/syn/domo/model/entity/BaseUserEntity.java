package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private LocalDate addedOn;

    public BaseUserEntity() {
    }

    @Column(name = "first_name", nullable = false, length = 55)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false, length = 55)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

}
