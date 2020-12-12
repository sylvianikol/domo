package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "buildings")
public class Building {

    private Long id;
    private String address;
    private int floorsNumber;
    private int apartmentsPerFloor;

    private Set<Floor> floors;

    public Building() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "floors_count", nullable = false)
    public int getFloorsNumber() {
        return floorsNumber;
    }

    public void setFloorsNumber(int floorsCount) {
        this.floorsNumber = floorsCount;
    }

    @Column(name = "apartments_per_floor", nullable = false)
    public int getApartmentsPerFloor() {
        return apartmentsPerFloor;
    }

    public void setApartmentsPerFloor(int apartmentsPerFloor) {
        this.apartmentsPerFloor = apartmentsPerFloor;
    }

    @OneToMany(mappedBy = "building", fetch = FetchType.EAGER)
    public Set<Floor> getFloors() {
        return floors;
    }

    public void setFloors(Set<Floor> floors) {
        this.floors = floors;
    }
}
