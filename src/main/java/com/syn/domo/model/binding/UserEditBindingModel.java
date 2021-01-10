package com.syn.domo.model.binding;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.RegexPatterns.PHONE_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class UserEditBindingModel extends BaseUserBindingModel {

    private String id;
    //    private String password;
    private String email;
    private String phoneNumber;

    public UserEditBindingModel() {
    }

    @NotNull(message = ID_NULL)
    @NotEmpty(message = ID_EMPTY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
