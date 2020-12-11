package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Floor does not exists!")
public class FloorNotFoundException extends RuntimeException {

    public FloorNotFoundException(String message) {
        super(message);
    }

    public FloorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
