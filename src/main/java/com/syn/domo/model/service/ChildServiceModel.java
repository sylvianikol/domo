package com.syn.domo.model.service;

import java.util.Set;

public class ChildServiceModel extends BaseUserEntityServiceModel {

    private Set<ResidentServiceModel> parents;

    public ChildServiceModel() {
    }

    public Set<ResidentServiceModel> getParents() {
        return parents;
    }

    public void setParents(Set<ResidentServiceModel> parents) {
        this.parents = parents;
    }
}
