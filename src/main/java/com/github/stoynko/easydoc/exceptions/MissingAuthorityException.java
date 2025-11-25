package com.github.stoynko.easydoc.exceptions;

public class MissingAuthorityException extends RuntimeException {

    public MissingAuthorityException() {
        super(ErrorMessages.MISSING_AUTHORITY.getErrorMessage());
    }

    public MissingAuthorityException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
