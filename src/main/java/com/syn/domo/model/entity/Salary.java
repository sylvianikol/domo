package com.syn.domo.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "salaries")
public class Salary extends BasePaymentEntity {

    private Staff staff;

    public Salary() {
    }

    public Salary(BigDecimal total, LocalDate issueDate, LocalDate dueDate, LocalDateTime paidDate, boolean isPaid, Staff staff) {
        super(total, issueDate, dueDate, paidDate, isPaid);
        this.staff = staff;
    }

    @ManyToOne(cascade = { MERGE, REFRESH })
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
