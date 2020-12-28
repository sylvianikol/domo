package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "residents")
public class Resident extends UserEntity {

    private Apartment apartment;
    Set<Child> children;

    public Resident() {
    }

    @ManyToOne
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @ManyToMany(mappedBy = "parents", fetch = FetchType.EAGER)
    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }
}
