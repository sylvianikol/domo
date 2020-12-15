package com.syn.domo.model.binding;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffAddBindingModel extends BaseUserAddBindingModel {

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
