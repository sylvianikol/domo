package com.syn.domo.model.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeeViewModel {

    private BigDecimal base;
    private BigDecimal total;
    private LocalDate startDate;
    private LocalDate dueDate;
    private boolean isPaid;
    private boolean isOverdue;
    private ApartmentViewModel apartment;

    public FeeViewModel() {
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public ApartmentViewModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentViewModel apartment) {
        this.apartment = apartment;
    }
}
