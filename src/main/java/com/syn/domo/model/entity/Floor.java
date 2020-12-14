package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "floors")
public class Floor extends BaseEntity {

    private int number;
    private Building building;

    Set<Apartment> apartments;

    public Floor() {
    }

    @Column(nullable = false, unique = true, updatable = false)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER)
    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }


}
