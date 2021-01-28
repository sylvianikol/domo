package com.syn.domo.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class SalaryServiceModel extends BaseServiceModel {

    private BigDecimal total;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime paidDate;
    private boolean isPaid;

    StaffServiceModel staff;

    public SalaryServiceModel() {
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
}
