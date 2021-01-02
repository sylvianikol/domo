package com.syn.domo.model.binding;

import javax.validation.constraints.*;

import java.time.LocalDate;

import static com.syn.domo.common.RegexPatterns.PHONE_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;
import static com.syn.domo.common.ValidationErrorMessages.EMAIL_INVALID;

public class UserBindingModel extends BaseUserBindingModel {

//    private String password;
    private String email;
    private String phoneNumber;
    private String userRole;
    private LocalDate addedOn;

    public UserBindingModel() {
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

    @NotNull(message = PHONE_NULL)
    @NotEmpty(message = PHONE_EMPTY)
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = PHONE_INVALID)
    @Size(max = 20, message = PHONE_LENGTH)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}