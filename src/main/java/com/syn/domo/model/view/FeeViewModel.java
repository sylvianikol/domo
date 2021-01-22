package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FeeViewModel {

    private String id;
    private BigDecimal total;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime paidDate;
    private boolean isPaid;
    private String payerId;
    @JsonManagedReference
    private ApartmentViewModel apartment;

    public FeeViewModel() {
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

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public ApartmentViewModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentViewModel apartment) {
        this.apartment = apartment;
    }
}
