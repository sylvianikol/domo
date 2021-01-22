package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "staff")
public class Staff extends UserEntity {

    private String job;
    private BigDecimal salary;
    private Set<Building> buildings;

    public Staff() {
        this.salary = BigDecimal.ZERO;
    }

    public Staff(String firstName, String lastName, LocalDate addedOn, String email, String password, String phoneNumber, boolean isActive, Set<Role> roles, String job, BigDecimal salary, Set<Building> buildings) {
        super(firstName, lastName, addedOn, email, password, phoneNumber, isActive, roles);
        this.job = job;
        this.salary = salary;
        this.buildings = buildings;
    }

    @Column(nullable = false, columnDefinition = "varchar(40)")
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @ColumnDefault("0")
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @ManyToMany(cascade = { MERGE, REFRESH }, fetch = EAGER)
    @JoinTable(name = "staff_buildings",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "building_id"))
    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }
}
