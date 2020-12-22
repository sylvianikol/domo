package com.syn.domo.model.binding;

import java.time.LocalDate;

public class BuildingEditBindingModel {

    private String name;
    private String address;

    public BuildingEditBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
