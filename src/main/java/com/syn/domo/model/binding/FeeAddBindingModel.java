package com.syn.domo.model.binding;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FeeAddBindingModel {

    private BigDecimal base;

    public FeeAddBindingModel() {
    }

    @NotNull
    @DecimalMin("0")
    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }
}
