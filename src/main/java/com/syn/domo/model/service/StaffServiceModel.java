package com.syn.domo.model.service;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.SALARY_MIN;

public class StaffServiceModel extends UserServiceModel {

    private String job;
    private BigDecimal salary;
    private Set<BuildingServiceModel> buildings;

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

    @NotNull(message = SALARY_NOT_NULL)
    @DecimalMin(value = "0", message = SALARY_MIN)
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
