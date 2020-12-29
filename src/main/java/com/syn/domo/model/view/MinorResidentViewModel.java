package com.syn.domo.model.view;

public class MinorResidentViewModel extends BaseUserEntityViewModel  {

    private String apartmentNumber;

    public MinorResidentViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
