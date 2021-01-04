package com.syn.domo.model.binding;

import java.util.Set;

public class ChildAddBindingModel extends BaseUserBindingModel {

    private Set<ParentBindingModel> parents;

    public ChildAddBindingModel() {
    }

    public Set<ParentBindingModel>  getParents() {
        return parents;
    }

    public void setParents(Set<ParentBindingModel>  parents) {
        this.parents = parents;
    }
}
