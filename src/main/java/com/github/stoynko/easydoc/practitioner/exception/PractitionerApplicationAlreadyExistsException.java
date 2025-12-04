package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.PRACTITIONER_APPLICATION_PENDING_EXISTS;

public class PractitionerApplicationAlreadyExistsException extends RuntimeException {

    public PractitionerApplicationAlreadyExistsException() {
        super(PRACTITIONER_APPLICATION_PENDING_EXISTS.getErrorMessage());
    }

}
