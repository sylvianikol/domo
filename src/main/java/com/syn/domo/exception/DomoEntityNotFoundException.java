package com.syn.domo.exception;

import com.syn.domo.error.ErrorContainer;

public class DomoEntityNotFoundException extends RuntimeException {

    private ErrorContainer errorContainer;

    public DomoEntityNotFoundException(String message) {
        super(message);
        this.errorContainer = new ErrorContainer();
    }

    public DomoEntityNotFoundException(String message, ErrorContainer errorContainer) {
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
