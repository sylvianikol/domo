package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND, reason = "Building found in archive!")
public class BuildingArchivedExistsException extends RuntimeException {

    public BuildingArchivedExistsException(String message) {
        super(message);
    }
}
