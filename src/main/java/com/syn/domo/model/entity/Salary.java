package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.math.BigDecimal;
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
