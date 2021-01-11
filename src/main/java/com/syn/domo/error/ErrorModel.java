package com.syn.domo.error;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashSet;

public class ErrorModel {

    Object object;
    private ErrorContainer errorContainer;

    public ErrorModel(Object object, BindingResult bindingResult) {
        this.object = object;
        this.setErrorContainer(bindingResult);
    }

    private void setErrorContainer(BindingResult bindingResult) {

        this.errorContainer = new ErrorContainer();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String key = error.getField();
            String value = error.getDefaultMessage();

            errorContainer.getErrors().putIfAbsent(key, new HashSet<>());
            errorContainer.getErrors().get(key).add(value);
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ErrorContainer getErrorContainer() {
        return errorContainer;
    }

    public void setViolations(ErrorContainer violations) {
        this.errorContainer = violations;
    }
}
