package com.syn.domo.model.service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.PASSWORD_INVALID_LENGTH;

public class UserActivateServiceModel {

    private String password;
    private String confirmPassword;

    public UserActivateServiceModel() {
    }

    @NotNull(message = PASSWORD_NOT_NULL)
    @NotEmpty(message = PASSWORD_NOT_EMPTY)
    @Size(min = 3, max = 30, message = PASSWORD_INVALID_LENGTH)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @NotNull(message = PASSWORD_NOT_NULL)
    @NotEmpty(message = PASSWORD_NOT_EMPTY)
    @Size(min = 3, max = 30, message = PASSWORD_INVALID_LENGTH)
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
