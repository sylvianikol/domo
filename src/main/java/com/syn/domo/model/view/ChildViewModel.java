package com.syn.domo.model.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Set;

public class ChildViewModel extends BaseUserViewModel {

    @JsonManagedReference
    private ApartmentViewModel apartment;
    @JsonManagedReference
    private Set<ResidentInnerViewModel> parents;

    public ChildViewModel() {
    }

   public Set<ResidentInnerViewModel> getParents() {
        return parents;
    }

    public void setParents(Set<ResidentInnerViewModel> parents) {
        this.parents = parents;
    }
}
