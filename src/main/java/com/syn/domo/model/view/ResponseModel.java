package com.syn.domo.model.view;

import com.syn.domo.error.ErrorContainer;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class ResponseModel<T> {

    private String id;
    private T object;
    private ErrorContainer errorContainer;

    public ResponseModel() {
        this.errorContainer = new ErrorContainer();
    }

    public ResponseModel(T object) {
        this();
        this.object = object;
    }

    public ResponseModel(String id, T object) {
        this(object);
        this.id = id;
    }

    public ResponseModel(T object, BindingResult bindingResult) {
        this(object);
        this.setErrorContainer(bindingResult);
    }

    public ResponseModel(T object, ErrorContainer errorContainer) {
        this(object);
        this.errorContainer = errorContainer;
    }

    public ResponseModel(T object, Set<ConstraintViolation<T>> constraintViolations) {
        this(object);
        this.setErrorContainer(constraintViolations);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private void setErrorContainer(BindingResult bindingResult) {
        for (FieldError error : bindingResult.getFieldErrors()) {
            String key = error.getField();
            String value = error.getDefaultMessage();

            errorContainer.getErrors().putIfAbsent(key, new HashSet<>());
            errorContainer.getErrors().get(key).add(value);
        }
    }

    public void setErrorContainer(Set<ConstraintViolation<T>> constraintViolations) {
        for (ConstraintViolation<T> violation : constraintViolations) {
            String key = violation.getPropertyPath().toString();
            String value = violation.getMessage();

            errorContainer.getErrors().putIfAbsent(key, new HashSet<>());
            errorContainer.getErrors().get(key).add(value);
        }
    }

    public void setErrorContainer(ErrorContainer errorContainer) {
        this.errorContainer = errorContainer;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public ErrorContainer getErrorContainer() {
        return errorContainer;
    }

    public void setViolations(ErrorContainer violations) {
        this.errorContainer = violations;
    }

    public boolean hasErrors() {
        return !this.getErrorContainer().getErrors().isEmpty();
    }

}
