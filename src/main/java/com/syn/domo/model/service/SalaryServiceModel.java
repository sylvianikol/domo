package com.syn.domo.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class SalaryServiceModel extends BaseServiceModel {

    private BigDecimal                total;
    private BigDecimal                unpaidTotal;
    private LocalDate                 issueDate;
    private LocalDate                 dueDate;
    private LocalDateTime             paidDate;
    private boolean                   isPaid;
    private StaffServiceModel         staff;
    private Set<BuildingServiceModel> buildings;

    public SalaryServiceModel() {
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUnpaidTotal() {
        return unpaidTotal;
    }

    public void setUnpaidTotal(BigDecimal unpaidTotal) {
        this.unpaidTotal = unpaidTotal;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public StaffServiceModel getStaff() {
        return staff;
    }

    public void setStaff(StaffServiceModel staff) {
        this.staff = staff;
    }

    public Set<BuildingServiceModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<BuildingServiceModel> buildings) {
        this.buildings = buildings;
    }
}
