package com.github.stoynko.easydoc.exceptions;

public class AlreadyOnboardedException extends RuntimeException {

    public AlreadyOnboardedException(ErrorMessages message) {
        super(message.getErrorMessage());
    }
}
