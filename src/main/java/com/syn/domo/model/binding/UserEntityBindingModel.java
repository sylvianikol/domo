package com.syn.domo.model.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_INVALID;

public abstract class UserEntityBindingModel extends BaseUserEntityBindingModel {

//    private String password;
    private String email;
    private String phoneNumber;

    public UserEntityBindingModel() {
    }

//    @NotNull(message = PASSWORD_NULL)
//    @NotEmpty(message = PASSWORD_EMPTY)
//    @Length(min = 3, max = 30, message = PASSWORD_INVALID)
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    @NotNull(message = EMAIL_NULL)
    @NotEmpty(message = EMAIL_EMPTY)
    @Email(message = EMAIL_INVALID)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
