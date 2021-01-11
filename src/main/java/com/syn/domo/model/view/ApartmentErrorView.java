package com.syn.domo.model.view;

import com.syn.domo.error.ViolationContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApartmentErrorView {

    private String number;
    private int floor;
    private int pets;
    private Map<String, Set<String>> errors;
    private ViolationContainer violations;

    public ApartmentErrorView() {
        this.errors = new HashMap<>();
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

    public Map<String, Set<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Set<String>> errors) {
        this.errors = errors;
    }

    public ViolationContainer getViolations() {
        return violations;
    }

    public void setViolations(ViolationContainer violations) {
        this.violations = violations;
    }
}
