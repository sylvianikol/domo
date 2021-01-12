package com.syn.domo.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class ParentBindingModel {

    private String id;

    public ParentBindingModel() {
    }

    @NotNull(message = ID_NULL)
    @NotEmpty(message = ID_EMPTY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
