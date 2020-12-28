package com.syn.domo.model.binding;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class ResidentEditBindingModel extends UserEntityBindingModel{

    private String id;
    private String userRole;
    private LocalDate addedOn;

    public ResidentEditBindingModel() {
    }

    @NotNull(message = ID_NULL)
    @NotEmpty(message = ID_EMPTY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = ROLE_NULL)
    @NotEmpty(message = ROLE_EMPTY)
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @NotNull(message = DATE_NULL)
    @PastOrPresent(message = DATE_INVALID)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }
}
