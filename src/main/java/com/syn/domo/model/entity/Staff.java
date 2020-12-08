package com.syn.domo.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff extends BaseUser {

    private LocalDate hiredOn;
    private LocalDate dismissedOn;
    private Job job;

    public Staff() {
    }

    @Column(name = "hired_on")
    public LocalDate getHiredOn() {
        return hiredOn;
    }

    public void setHiredOn(LocalDate hiredDate) {
        this.hiredOn = hiredDate;
    }

    @Column(name = "dismissed_on")
    public LocalDate getDismissedOn() {
        return dismissedOn;
    }

    public void setDismissedOn(LocalDate releasedDate) {
        this.dismissedOn = releasedDate;
    }

    @ManyToOne
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
