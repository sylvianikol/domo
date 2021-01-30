package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "salaries")
public class Salary extends BasePaymentEntity {

    private BigDecimal    unpaidTotal;
    private Staff         staff;
    private Set<Building> buildings;

    public Salary() {
    }

    public Salary(BigDecimal total, LocalDate issueDate, LocalDate dueDate, LocalDateTime paidDate, boolean isPaid, BigDecimal unpaidTotal, Staff staff, Set<Building> buildings) {
        super(total, issueDate, dueDate, paidDate, isPaid);
        this.unpaidTotal = unpaidTotal;
        this.staff = staff;
        this.buildings = buildings;
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

    @ManyToMany(cascade = { REFRESH }, fetch = EAGER)
    @JoinTable(name = "salaries_buildings",
            joinColumns = @JoinColumn(name = "salary_id"),
            inverseJoinColumns = @JoinColumn(name = "building_id"))
    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> debtors) {
        this.buildings = debtors;
    }
}
