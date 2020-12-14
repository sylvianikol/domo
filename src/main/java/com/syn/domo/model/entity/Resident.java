package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "residents")
public class Resident extends BaseUser {

    private LocalDate movedIn;
    private LocalDate movedOut;
    private Apartment apartment;

    public Resident() {
    }

    @Column(name = "moved_in", nullable = false)
    public LocalDate getMovedIn() {
        return movedIn;
    }

    public void setMovedIn(LocalDate registeredOn) {
        this.movedIn = registeredOn;
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
