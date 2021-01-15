package com.syn.domo.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class UserActivateBindingModel {

    private String password;
    private String confirmPassword;

    public UserActivateBindingModel() {
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

    @NotNull(message = PASSWORD_NOT_NULL)
    @NotEmpty(message = PASSWORD_NOT_EMPTY)
    @Size(min = 3, max = 30, message = PASSWORD_INVALID_LENGTH)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
