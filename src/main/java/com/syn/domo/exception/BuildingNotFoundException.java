package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Building not found")
public class BuildingNotFoundException extends RuntimeException {

    public BuildingNotFoundException(String message) {
        super(message);
    }
}
