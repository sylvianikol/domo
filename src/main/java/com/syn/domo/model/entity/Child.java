package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "children")
public class Child extends BaseUserEntity {

    private Apartment apartment;
    Set<Resident> parents;

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
            joinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"))
    public Set<Resident> getParents() {
        return parents;
    }

    public void setParents(Set<Resident> parents) {
        this.parents = parents;
    }
}
