package com.github.stoynko.easydoc.exceptions;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
