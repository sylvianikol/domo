package com.syn.domo.model.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.DATE_INVALID;

public class ChildEditBindingModel extends BaseUserEntityBindingModel {

    private String id;
    private LocalDate addedOn;

    public ChildEditBindingModel() {
    }

    @NotNull(message = ID_NULL)
    @NotEmpty(message = ID_EMPTY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = DATE_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = DATE_INVALID)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }
}
