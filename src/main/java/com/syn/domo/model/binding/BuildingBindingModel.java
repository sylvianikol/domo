package com.syn.domo.model.binding;

import javax.validation.constraints.*;

import java.math.BigDecimal;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class BuildingBindingModel {

    private String name;
    private String neighbourhood;
    private String address;
    private int floors;
    private BigDecimal budget;
    private BigDecimal baseFee;

    public BuildingBindingModel() {
    }

    public BuildingBindingModel(String name, String neighbourhood, String address, int floors, BigDecimal budget, BigDecimal baseFee) {
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.address = address;
        this.floors = floors;
        this.budget = budget;
        this.baseFee = baseFee;
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
}
