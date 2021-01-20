package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class BuildingViewModel {

    private String id;
    private String name;
    private String neighbourhood;
    private String address;
    private int floors;
    private BigDecimal budget;
    private BigDecimal baseFee;
    private LocalDate addedOn;

    @JsonBackReference
    private Set<ApartmentViewModel> apartments;
    @JsonBackReference
    private Set<StaffViewModel> staff;

    public BuildingViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(BigDecimal baseFee) {
        this.baseFee = baseFee;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public Set<ApartmentViewModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentViewModel> apartments) {
        this.apartments = apartments;
    }

    public Set<StaffViewModel> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffViewModel> staff) {
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingViewModel)) return false;
        BuildingViewModel that = (BuildingViewModel) o;
        return floors == that.floors &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(neighbourhood, that.neighbourhood) &&
                Objects.equals(address, that.address) &&
                Objects.equals(budget, that.budget) &&
                Objects.equals(baseFee, that.baseFee) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, neighbourhood, address, floors, budget, baseFee, addedOn);
    }
}
