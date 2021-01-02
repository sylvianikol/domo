package com.syn.domo.model.view;

import java.util.Objects;
import java.util.Set;

public class ChildViewModel extends BaseUserViewModel {

    private String apartmentNumber;
    private Set<UserViewModel> parents;

    public ChildViewModel() {
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Set<UserViewModel> getParents() {
        return parents;
    }

    public void setParents(Set<UserViewModel> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildViewModel)) return false;
        if (!super.equals(o)) return false;
        ChildViewModel that = (ChildViewModel) o;
        return Objects.equals(apartmentNumber, that.apartmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartmentNumber);
    }
}
