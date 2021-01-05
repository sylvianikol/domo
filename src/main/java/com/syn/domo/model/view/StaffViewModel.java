package com.syn.domo.model.view;

import java.math.BigDecimal;
import java.util.Set;

public class StaffViewModel extends UserViewModel {

    private String job;
    private BigDecimal salary;
    private Set<BuildingInnerViewModel> buildings;

    public StaffViewModel() {
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

    public Set<BuildingInnerViewModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingInnerViewModel> buildings) {
        this.buildings = buildings;
    }
}
