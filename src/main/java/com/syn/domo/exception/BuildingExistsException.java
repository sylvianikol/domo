package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND, reason = "Building already exists")
public class BuildingExistsException extends RuntimeException {

    public BuildingExistsException(String message) {
        super(message);
    }
}
