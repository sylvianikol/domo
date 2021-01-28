package com.syn.domo.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "fees")
public class Fee extends BasePaymentEntity {

    private String payerId;
    private Apartment apartment;

    public Fee() {
    }

    public Fee(BigDecimal total, LocalDate issueDate, LocalDate dueDate, LocalDateTime paidDate, boolean isPaid, String payerId, Apartment apartment) {
        super(total, issueDate, dueDate, paidDate, isPaid);
        this.payerId = payerId;
        this.apartment = apartment;
    }

    @Column(name = "payer_id")
    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    @ManyToOne(cascade = { MERGE, REFRESH })
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
}
