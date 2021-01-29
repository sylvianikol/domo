package com.syn.domo.model.service;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffServiceModel extends UserServiceModel {

    private String job;
    private String wage;
    private Set<BuildingServiceModel> buildings;
    private Set<SalaryServiceModel>   salaries;

    public StaffServiceModel() {
    }

    @NotNull(message = JOB_NOT_NULL)
    @NotEmpty(message = JOB_NOT_EMPTY)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @NotNull(message = WAGE_NOT_NULL)
    @DecimalMin(value = "0", message = WAGE_MIN)
    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public Set<BuildingServiceModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingServiceModel> buildings) {
        this.buildings = buildings;
    }

    public Set<SalaryServiceModel> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<SalaryServiceModel> salaries) {
        this.salaries = salaries;
    }
}
