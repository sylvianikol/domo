package com.syn.domo.model.view;

public class ApartmentAddViewModel {

    private String number;
    private int floor;
    private int pets;

    public ApartmentAddViewModel() {
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

}
