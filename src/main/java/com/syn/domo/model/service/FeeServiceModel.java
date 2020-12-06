package com.syn.domo.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeeServiceModel extends BaseServiceModel {

    private BigDecimal total;
    private LocalDate startDate;
    private LocalDate dueDate;
    private boolean isPaid;
    private String ApartmentNumber;

    public FeeServiceModel() {
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

    public String getApartmentNumber() {
        return ApartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        ApartmentNumber = apartmentNumber;
    }
}
