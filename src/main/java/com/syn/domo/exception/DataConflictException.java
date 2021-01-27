package com.syn.domo.exception;

import com.syn.domo.error.ErrorContainer;

public class DataConflictException extends RuntimeException {

    private ErrorContainer errorContainer;

    public DataConflictException(String message) {
        super(message);
        this.errorContainer = new ErrorContainer();
    }

    public DataConflictException(String message, ErrorContainer errorContainer) {
        super(message);
        this.errorContainer = errorContainer;
    }

    public ErrorContainer getErrorContainer() {
        return errorContainer;
    }

    public void setErrorContainer(ErrorContainer errorContainer) {
        this.errorContainer = errorContainer;
    }
}
