package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class SalaryViewModel {

    private String                 id;
    private BigDecimal             total;
    private BigDecimal             unpaidTotal;
    private LocalDate              issueDate;
    private LocalDate              dueDate;
    private LocalDateTime          paidDate;
    private boolean                isPaid;
    private Set<BuildingViewModel> debtors;

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

    public StaffViewModel getStaff() {
        return staff;
    }

    public void setStaff(StaffViewModel staff) {
        this.staff = staff;
    }

    public Set<BuildingViewModel> getDebtors() {
        return debtors;
    }

    public void setDebtors(Set<BuildingViewModel> debtors) {
        this.debtors = debtors;
    }
}
