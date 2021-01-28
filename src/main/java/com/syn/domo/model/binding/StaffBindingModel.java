package com.syn.domo.model.binding;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static com.syn.domo.common.RegexPatterns.PHONE_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffBindingModel extends BaseUserBindingModel {

    private String email;
    private String phoneNumber;
    private String job;
    private BigDecimal wage;

    public StaffBindingModel() {
    }

    public StaffBindingModel(String firstName, String lastName, String email, String phoneNumber, String job, BigDecimal wage) {
        super(firstName, lastName);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.job = job;
        this.wage = wage;
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

    @NotNull(message = JOB_NOT_NULL)
    @NotEmpty(message = JOB_NOT_EMPTY)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @NotNull(message = WAGE_NOT_NULL)
    @DecimalMin(value = "0", message = WAGE_MIN)
    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }
}
