package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "floors")
public class Floor extends BaseEntity {

    private int number;
    private int capacity;
    private boolean hasCapacity;
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

    @Column(nullable = false)
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int apartmentsNumber) {
        this.capacity = apartmentsNumber;
    }

    @Transient
    public boolean getHasCapacity() {
        return this.getApartments().size() < this.getCapacity();
    }

    public void setHasCapacity(boolean hasCapacity) {
        this.hasCapacity = hasCapacity;
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
