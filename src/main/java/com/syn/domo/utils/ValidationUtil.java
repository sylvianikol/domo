package com.syn.domo.utils;

import com.syn.domo.exception.error.ErrorContainer;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T entity);

    <T> Set<ConstraintViolation<T>> violations(T entity);

    <T> ErrorContainer getViolations(T entity);
}
