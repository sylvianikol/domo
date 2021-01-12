package com.syn.domo.model.binding;

import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.syn.domo.common.ValidationErrorMessages.PARENTS_NULL;

public class ChildAddBindingModel extends BaseUserBindingModel {

    private Set<ParentBindingModel> parents;

    public ChildAddBindingModel() {
    }

    @NotNull(message = PARENTS_NULL)
    public Set<ParentBindingModel>  getParents() {
        return parents;
    }

    public void setParents(Set<ParentBindingModel>  parents) {
        this.parents = parents;
    }
}
