package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "children")
public class Child extends BaseUserEntity {

    private Apartment apartment;
    Set<UserEntity> parents;

    public Child() {
    }

    @ManyToOne
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @ManyToMany(cascade = { MERGE, REFRESH }, fetch = EAGER)
    @JoinTable(name = "children_parents",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    public Set<UserEntity> getParents() {
        return parents;
    }

    public void setParents(Set<UserEntity> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Child)) return false;
        if (!super.equals(o)) return false;
        Child child = (Child) o;
        return Objects.equals(apartment, child.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartment);
    }
}
