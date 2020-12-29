package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUserEntity)) return false;
        if (!super.equals(o)) return false;
        BaseUserEntity that = (BaseUserEntity) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, addedOn);
    }
}
