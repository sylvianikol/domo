package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "buildings")
public class Building extends BaseEntity{

    private String name;
    private String address;
    private LocalDate addedOn;
    private LocalDate archivedOn;

    private Set<Floor> floors;
    private Set<Apartment> apartments;

    public Building() {
    }

    @Column(nullable = false)
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

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @Column(name = "archived_on")
    public LocalDate getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(LocalDate removedOn) {
        this.archivedOn = removedOn;
    }

   @OneToMany(mappedBy = "building", fetch = FetchType.EAGER)
    public Set<Floor> getFloors() {
        return floors;
    }

    public void setFloors(Set<Floor> floors) {
        this.floors = floors;
    }

    @OneToMany(mappedBy = "building", fetch = FetchType.EAGER)
    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }
}
