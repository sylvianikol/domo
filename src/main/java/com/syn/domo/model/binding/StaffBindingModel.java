package com.syn.domo.model.binding;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static com.syn.domo.common.RegexPatterns.PHONE_NUMBER_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffBindingModel extends BaseUserBindingModel {

    //    private String password;
    private String email;
    private String phoneNumber;
    private String job;
    private BigDecimal salary;

    public StaffBindingModel() {
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

    @NotNull(message = JOB_NULL)
    @NotEmpty(message = JOB_EMPTY)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @NotNull(message = SALARY_NULL)
    @DecimalMin(value = "0", message = SALARY_MIN)
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
