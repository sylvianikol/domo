package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resident not found")
public class ResidentNotFoundException extends RuntimeException {

    public ResidentNotFoundException(String message) {
        super(message);
    }
}
