package com.syn.domo.model.binding;

import java.util.Set;

public class ChildAddBindingModel extends BaseUserBindingModel {

    private Set<UserBindingModel> parents;

    public ChildAddBindingModel() {
    }

    public Set<UserBindingModel>  getParents() {
        return parents;
    }

    public void setParents(Set<UserBindingModel>  parents) {
        this.parents = parents;
    }
}
