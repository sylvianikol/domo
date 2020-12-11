package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "floors")
public class Floor extends BaseEntity {

    private int number;
    private int apartmentsPerFloor;
    private boolean hasCapacity;

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

    @Column(name = "apartments_per_floor", nullable = false)
    public int getApartmentsPerFloor() {
        return apartmentsPerFloor;
    }

    public void setApartmentsPerFloor(int apartmentsNumber) {
        this.apartmentsPerFloor = apartmentsNumber;
    }

    @Transient
    public boolean getHasCapacity() {
        return this.getApartments().size() < this.getApartmentsPerFloor();
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
}
