package com.syn.domo.model.binding;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.syn.domo.common.ValidationErrorMessages.*;

public class StaffAddBindingModel extends BaseUserAddBindingModel {

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;

    public StaffAddBindingModel() {
    }

    @NotNull(message = SALARY_NULL)
    @DecimalMin(value = "0", message = SALARY_INVALID)
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @NotNull(message = DATE_NULL)
    @PastOrPresent(message = START_DATE_INVALID)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @FutureOrPresent(message = END_DATE_INVALID)
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
