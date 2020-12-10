package com.syn.domo.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "floors")
public class Floor extends BaseEntity {

    private int number;
    private int apartmentsPerFloor;

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
}
