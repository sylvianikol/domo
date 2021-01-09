package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "buildings")
public class Building extends BaseEntity{

    private String name;
    private String neighbourhood;
    private String address;
    private int floors;
    private LocalDate addedOn;

    private Set<Apartment> apartments;
    private Set<Staff> staff;

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
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @Column(nullable = false, unique = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(nullable = false)
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    @OneToMany(mappedBy = "building", cascade = REMOVE, fetch = EAGER)
    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @ManyToMany(mappedBy = "buildings", cascade = { MERGE, REFRESH }, fetch = EAGER)
    public Set<Staff> getStaff() {
        return staff;
    }

    public void setStaff(Set<Staff> staff) {
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;
        if (!super.equals(o)) return false;
        Building building = (Building) o;
        return floors == building.floors &&
                Objects.equals(name, building.name) &&
                Objects.equals(neighbourhood, building.neighbourhood) &&
                Objects.equals(address, building.address) &&
                Objects.equals(addedOn, building.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, neighbourhood, address, floors, addedOn);
    }
}
