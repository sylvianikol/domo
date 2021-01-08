package com.syn.domo.model.binding;

import javax.validation.constraints.NotNull;

import static com.syn.domo.common.ValidationErrorMessages.PAID_NULL;

public class FeeEditBindingModel {

    private boolean isPaid;

    public FeeEditBindingModel() {
    }

    @NotNull(message = PAID_NULL)
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
