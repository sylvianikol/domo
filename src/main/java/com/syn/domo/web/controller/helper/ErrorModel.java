package com.syn.domo.web.controller.helper;

import com.syn.domo.error.ViolationContainer;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashSet;

public class ErrorModel {

    Object object;
    private ViolationContainer violations;

    public ErrorModel(Object object, BindingResult bindingResult) {
        this.object = object;
        this.setViolations(bindingResult);
    }

    private void setViolations(BindingResult bindingResult) {
        this.violations = new ViolationContainer();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String key = error.getField();
            String value = error.getDefaultMessage();

            violations.getErrors().putIfAbsent(key, new HashSet<>());
            violations.getErrors().get(key).add(value);
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ViolationContainer getViolations() {
        return violations;
    }

    public void setViolations(ViolationContainer violations) {
        this.violations = violations;
    }
}
