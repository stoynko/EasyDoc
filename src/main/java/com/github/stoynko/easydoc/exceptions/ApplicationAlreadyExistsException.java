package com.github.stoynko.easydoc.exceptions;

public class ApplicationAlreadyExistsException extends RuntimeException {

    public ApplicationAlreadyExistsException() {
        super(ErrorMessages.PRACTITIONER_APPLICATION_PENDING_EXISTS.getErrorMessage());
    }

    public ApplicationAlreadyExistsException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
