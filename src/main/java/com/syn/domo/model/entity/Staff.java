package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff extends BaseUser {

    private Job job;

    public Staff() {
    }

    @ManyToOne
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
