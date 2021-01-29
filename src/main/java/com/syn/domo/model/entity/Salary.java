package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "salaries")
public class Salary extends BasePaymentEntity {

    private BigDecimal    unpaidTotal;
    private Staff         staff;
    private Set<Building> debtors;

    public Salary() {
    }

    @Column(name = "unpaid_total")
    @ColumnDefault("0")
    public BigDecimal getUnpaidTotal() {
        return unpaidTotal;
    }

    public void setUnpaidTotal(BigDecimal unpaidTotal) {
        this.unpaidTotal = unpaidTotal;
    }

    @ManyToOne(cascade = { MERGE, REFRESH })
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @OneToMany(cascade = { MERGE, REFRESH }, fetch = LAZY)
    public Set<Building> getDebtors() {
        return debtors;
    }

    public void setDebtors(Set<Building> debtors) {
        this.debtors = debtors;
    }
}
