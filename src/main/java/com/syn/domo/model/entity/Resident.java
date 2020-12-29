package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resident)) return false;
        if (!super.equals(o)) return false;
        Resident resident = (Resident) o;
        return Objects.equals(apartment.getId(), resident.apartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartment);
    }
}
