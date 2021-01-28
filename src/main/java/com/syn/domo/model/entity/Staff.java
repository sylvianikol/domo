package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "staff")
public class Staff extends UserEntity {

    private String job;
    private BigDecimal wage;

    private Set<Building> buildings;
    private Set<Salary> salaries;

    public Staff() {
    }

    public Staff(String firstName, String lastName, LocalDate addedOn, String email, String password, String phoneNumber, boolean isActive, Set<Role> roles, String job, BigDecimal wage, Set<Building> buildings, Set<Salary> salaries) {
        super(firstName, lastName, addedOn, email, password, phoneNumber, isActive, roles);
        this.job = job;
        this.wage = wage;
        this.buildings = buildings;
        this.salaries = salaries;
    }

    @Column(nullable = false, columnDefinition = "varchar(40)")
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @ColumnDefault("0")
    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    @ManyToMany(cascade = { REFRESH }, fetch = EAGER)
    @JoinTable(name = "staff_buildings",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "building_id"))
    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }

    @OneToMany(mappedBy = "staff", cascade = REMOVE, fetch = EAGER)
    public Set<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<Salary> salaries) {
        this.salaries = salaries;
    }
}
