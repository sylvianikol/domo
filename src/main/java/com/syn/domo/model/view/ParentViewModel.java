package com.syn.domo.model.view;

public class ParentViewModel extends UserEntityViewModel  {

    private String apartmentNumber;

    public ParentViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
