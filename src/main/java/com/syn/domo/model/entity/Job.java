package com.syn.domo.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    private String role;
    private BigDecimal wage;

    public Job() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String position) {
        this.role = position;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }
}
