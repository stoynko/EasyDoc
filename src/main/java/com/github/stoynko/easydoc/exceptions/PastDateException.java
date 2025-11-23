package com.github.stoynko.easydoc.exceptions;

public class PastDateException extends RuntimeException {

    public PastDateException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
