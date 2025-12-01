package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.PRACTITIONER_APPLICATION_PENDING_EXISTS;

public class ApplicationAlreadyExistsException extends RuntimeException {

    public ApplicationAlreadyExistsException() {
        super(PRACTITIONER_APPLICATION_PENDING_EXISTS.getErrorMessage());
    }

}
