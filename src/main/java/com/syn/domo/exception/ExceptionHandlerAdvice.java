package com.syn.domo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    // TODO: log exceptions

    @ExceptionHandler(BuildingAlreadyExistsException.class)
    public ResponseEntity<?> handleBuildingExistsException(BuildingAlreadyExistsException ex) {
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleResidentNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleResidentAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ChildNotFoundException.class)
    public ResponseEntity<?> handleChildNotFoundException(ChildNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ChildAlreadyExists.class)
    public ResponseEntity<?> handleChildAlreadyExists(ChildAlreadyExists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<?> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(ex.getMessage());
    }


}
