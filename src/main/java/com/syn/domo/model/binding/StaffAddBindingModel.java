package com.syn.domo.model.binding;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffAddBindingModel extends UserAddBindingModel {

    private String job;
    private BigDecimal salary;

    public StaffAddBindingModel() {
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
