package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.util.Set;

public class StaffViewModel extends UserViewModel {

    private String job;
    private String wage;
    private BigDecimal salary;
    @JsonManagedReference
    private Set<BuildingViewModel> buildings;

    public StaffViewModel() {
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<BuildingViewModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingViewModel> buildings) {
        this.buildings = buildings;
    }
}
