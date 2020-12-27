package com.syn.domo.model.binding;

public class StaffAddBindingModel extends UserEntityBindingModel {

    private String job;

    public StaffAddBindingModel() {
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
