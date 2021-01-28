package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SalaryViewModel {

    private String id;
    private BigDecimal total;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime paidDate;
    private boolean isPaid;

    @JsonManagedReference
    private StaffViewModel staff;

    public SalaryViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public StaffViewModel getStaff() {
        return staff;
    }

    public void setStaff(StaffViewModel staff) {
        this.staff = staff;
    }
}
