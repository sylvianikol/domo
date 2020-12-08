package com.syn.domo.model.service;

import java.util.Set;

public class ApartmentServiceModel extends BaseServiceModel {

    private String number;
    private Integer floor;
    private int pets;
    private Set<ResidentServiceModel> residents;
    private Set<ChildServiceModel> children;

    public ApartmentServiceModel() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }


    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public Set<ResidentServiceModel> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentServiceModel> residents) {
        this.residents = residents;
    }

    public Set<ChildServiceModel> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildServiceModel> children) {
        this.children = children;
    }
}
