package com.syn.domo.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.syn.domo.common.ValidationErrorMessages.ID_EMPTY;
import static com.syn.domo.common.ValidationErrorMessages.ID_NULL;

public class StaffIdBindingModel {

    private String id;

    public StaffIdBindingModel() {
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
