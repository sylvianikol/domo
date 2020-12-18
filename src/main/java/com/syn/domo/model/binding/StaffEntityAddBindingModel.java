package com.syn.domo.model.binding;

public class StaffEntityAddBindingModel extends UserEntityAddBindingModel {

    private String job;

    public StaffEntityAddBindingModel() {
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
