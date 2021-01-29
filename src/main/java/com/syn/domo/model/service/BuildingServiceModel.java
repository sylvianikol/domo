package com.syn.domo.model.service;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class BuildingServiceModel extends BaseServiceModel {

    private String     name;
    private String     address;
    private String     neighbourhood;
    private int        floors;
    private BigDecimal budget;
    private BigDecimal baseFee;
    private LocalDate  addedOn;

    private Set<ApartmentServiceModel> apartments;
    private Set<StaffServiceModel>     staff;
    private Set<SalaryServiceModel>    salaries;

    public BuildingServiceModel() {
    }

    public BuildingServiceModel(String name, String address, String neighbourhood, int floors, BigDecimal budget, BigDecimal baseFee, LocalDate addedOn) {
        this.name = name;
        this.address = address;
        this.neighbourhood = neighbourhood;
        this.floors = floors;
        this.budget = budget;
        this.baseFee = baseFee;
        this.addedOn = addedOn;
    }

    @NotNull(message = BUILDING_NAME_NOT_NULL)
    @NotEmpty(message = BUILDING_NAME_NOT_EMPTY)
    @Size(max = 40, message = BUILDING_NAME_INVALID_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = NEIGHBOURHOOD_NOT_NULL)
    @NotEmpty(message = NEIGHBOURHOOD_NOT_EMPTY)
    @Size(max = 40, message = NEIGHBOURHOOD_INVALID_LENGTH)
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @NotNull(message = ADDRESS_NOT_NULL)
    @NotEmpty(message = ADDRESS_NOT_EMPTY)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull(message = FLOOR_NUMBER_NOT_NULL)
    @Min(value = 0, message = FLOOR_MIN)
    @Max(value = 100, message = FLOOR_MAX_INVALID)
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    @NotNull(message = BUDGET_NOT_NULL)
    @DecimalMin(value = "0", message = BUDGET_MIN)
    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @NotNull(message = BASE_FEE_NOT_NULL)
    @DecimalMin(value = "0", message = BASE_FEE_MIN)
    public BigDecimal getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(BigDecimal baseFee) {
        this.baseFee = baseFee;
    }

    @Future(message = DATE_FUTURE)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public Set<ApartmentServiceModel> getApartments() {
        return apartments;
    }

    public void setApartments(Set<ApartmentServiceModel> apartments) {
        this.apartments = apartments;
    }

    public Set<StaffServiceModel> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffServiceModel> staff) {
        this.staff = staff;
    }

    public Set<SalaryServiceModel> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<SalaryServiceModel> salaries) {
        this.salaries = salaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildingServiceModel)) return false;
        if (!super.equals(o)) return false;
        BuildingServiceModel that = (BuildingServiceModel) o;
        return floors == that.floors &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(neighbourhood, that.neighbourhood) &&
                Objects.equals(budget, that.budget) &&
                Objects.equals(baseFee, that.baseFee) &&
                Objects.equals(addedOn, that.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address, neighbourhood, floors, budget, baseFee, addedOn);
    }
}
