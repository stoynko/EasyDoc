package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.PRACTITIONER_APPLICATION_PENDING_EXISTS;

public class ApplicationAlreadyExistsException extends RuntimeException {

    public ApplicationAlreadyExistsException() {
        super(PRACTITIONER_APPLICATION_PENDING_EXISTS.getErrorMessage());
    }

}
