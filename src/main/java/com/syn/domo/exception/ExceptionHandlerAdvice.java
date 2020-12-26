package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    // TODO: log exceptions

    @ExceptionHandler(BuildingExistsException.class)
    public ResponseEntity<?> handleBuildingExistsException(BuildingExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BuildingNotFoundException.class)
    public ResponseEntity<?> handleBuildingNotFoundException(BuildingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ApartmentAlreadyExistsException.class)
    public ResponseEntity<?> handleApartmentAlreadyExistsException(ApartmentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ApartmentNotFoundException.class)
    public ResponseEntity<?> handleApartmentNotFoundException(ApartmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<?> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(ex.getMessage());
    }


}
