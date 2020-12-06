package com.syn.domo.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff extends BaseUser {

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private JobRole jobRole;

    public Staff() {
    }

    @Column
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Column(name = "start_date")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate hiredDate) {
        this.startDate = hiredDate;
    }

    @Column(name = "end_date")
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate releasedDate) {
        this.endDate = releasedDate;
    }

    @Enumerated(EnumType.STRING)
    public JobRole getJobType() {
        return jobRole;
    }

    public void setJobType(JobRole jobRole) {
        this.jobRole = jobRole;
    }
}
