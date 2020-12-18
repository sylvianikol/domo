package com.syn.domo.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "staff")
public class Staff extends UserEntity {

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
