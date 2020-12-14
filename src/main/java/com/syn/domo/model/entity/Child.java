package com.syn.domo.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "children")
public class Child extends BaseEntity {

    private String firstName;
    private String lastName;
    private LocalDate movedIn;
    private LocalDate movedOut;
    private Apartment apartment;

    public Child() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "moved_in", nullable = false)
    public LocalDate getMovedIn() {
        return movedIn;
    }

    public void setMovedIn(LocalDate movedIn) {
        this.movedIn = movedIn;
    }

    @Column(name = "moved_out")
    public LocalDate getMovedOut() {
        return movedOut;
    }

    public void setMovedOut(LocalDate movedOut) {
        this.movedOut = movedOut;
    }

    @ManyToOne
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
}
