package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Set;

public class StaffViewModel extends UserViewModel {

    private String job;
    private String wage;
    @JsonManagedReference
    private Set<BuildingViewModel> buildings;
    @JsonBackReference
    private Set<SalaryViewModel>   salaries;

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

    public Set<BuildingViewModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingViewModel> buildings) {
        this.buildings = buildings;
    }

    public Set<SalaryViewModel> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<SalaryViewModel> salaries) {
        this.salaries = salaries;
    }
}
