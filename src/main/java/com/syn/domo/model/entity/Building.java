package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "buildings")
public class Building extends BaseEntity{

    private String name;
    private String address;
    private int floorsNumber;
    private LocalDate addedOn;
    private LocalDate removedOn;

    private Set<Floor> floors;

    public Building() {
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

   @OneToMany(mappedBy = "building", fetch = FetchType.EAGER)
    public Set<Floor> getFloors() {
        return floors;
    }

    public void setFloors(Set<Floor> floors) {
        this.floors = floors;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Column(name = "removed_on")
    public LocalDate getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(LocalDate removedOn) {
        this.removedOn = removedOn;
    }
}
