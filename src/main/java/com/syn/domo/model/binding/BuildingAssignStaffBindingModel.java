package com.syn.domo.model.binding;

import java.util.Set;

public class BuildingAssignStaffBindingModel {

    private Set<StaffIdBindingModel> staff;

    public BuildingAssignStaffBindingModel() {
    }

    public Set<StaffIdBindingModel> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffIdBindingModel> staff) {
        this.staff = staff;
    }
}
