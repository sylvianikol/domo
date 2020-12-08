package com.syn.domo.model.service;

import java.math.BigDecimal;

public class JobServiceModel extends BaseServiceModel {

    private String role;
    private BigDecimal wage;

    public JobServiceModel() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }
}
