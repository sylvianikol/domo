package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "residents")
public class Resident extends UserEntity {

    private Set<Apartment> apartments;
    private Set<Child> children;

    public Resident() {
    }

    public Resident(String firstName, String lastName, LocalDate addedOn, String email, String password, String phoneNumber, boolean isActive, Set<Role> roles, Set<Apartment> apartments) {
        super(firstName, lastName, addedOn, email, password, phoneNumber, isActive, roles);
        this.apartments = apartments;
    }

    @ManyToMany(cascade = { MERGE, REFRESH }, fetch = EAGER)
    @JoinTable(name = "residents_apartments",
            joinColumns = @JoinColumn(name = "resident_id"),
            inverseJoinColumns = @JoinColumn(name = "apartment_id"))
    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @ManyToMany(mappedBy = "parents", fetch = EAGER)
    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }
}
