package com.syn.domo.model.view;

import java.util.Set;

public class ChildViewModel extends BaseUserViewModel {

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
