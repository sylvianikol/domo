package com.syn.domo.model.view;

import com.syn.domo.error.ViolationContainer;

public class ApartmentErrorView {

    private String number;
    private int floor;
    private int pets;

    private ViolationContainer violations;

    public ApartmentErrorView() {
        this.violations = new ViolationContainer();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public ViolationContainer getViolations() {
        return violations;
    }

    public void setViolations(ViolationContainer violations) {
        this.violations = violations;
    }
}
