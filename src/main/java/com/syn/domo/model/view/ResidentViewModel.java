package com.syn.domo.model.view;

import java.util.Objects;
import java.util.Set;

public class ResidentViewModel extends UserEntityViewModel {

    private String apartmentNumber;

    private Set<MinorResidentViewModel> children;

    public ResidentViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Set<MinorResidentViewModel> getChildren() {
        return children;
    }

    public void setChildren(Set<MinorResidentViewModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResidentViewModel)) return false;
        if (!super.equals(o)) return false;
        ResidentViewModel that = (ResidentViewModel) o;
        return Objects.equals(apartmentNumber, that.apartmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartmentNumber);
    }
}
