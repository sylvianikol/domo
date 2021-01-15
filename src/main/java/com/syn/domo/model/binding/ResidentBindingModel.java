package com.syn.domo.model.binding;

import javax.validation.constraints.*;

import static com.syn.domo.common.RegexPatterns.PHONE_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class ResidentBindingModel extends BaseUserBindingModel {

    private String email;
    private String phoneNumber;

    public ResidentBindingModel() {
    }

    @NotNull(message = EMAIL_NOT_NULL)
    @NotEmpty(message = EMAIL_NOT_EMPTY)
    @Email(message = EMAIL_INVALID)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = PHONE_NOT_NULL)
    @NotEmpty(message = PHONE_NOT_EMPTY)
    @Pattern(regexp = PHONE_REGEX, message = PHONE_INVALID)
    @Size(max = 20, message = PHONE_LENGTH)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
