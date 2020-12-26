package com.syn.domo.model;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ErrorResponse {

    private Object target;
    private List<ObjectError> errors;

    public ErrorResponse(Object target, List<ObjectError> errors) {
        this.target = target;
        this.errors = errors;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
