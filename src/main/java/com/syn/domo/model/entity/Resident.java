package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "residents")
public class Resident extends BaseUser {

    private Apartment apartment;

    public Resident() {
    }

    @ManyToOne
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
}
