package com.syn.domo.model.service;

import java.math.BigDecimal;
import java.util.Set;

public class StaffServiceModel extends UserServiceModel {

    private String job;
    private BigDecimal salary;
    private Set<BuildingServiceModel> buildings;

    public StaffServiceModel() {
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<BuildingServiceModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingServiceModel> buildings) {
        this.buildings = buildings;
    }
}
