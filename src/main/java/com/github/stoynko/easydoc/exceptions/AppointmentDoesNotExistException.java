package com.github.stoynko.easydoc.exceptions;

public class AppointmentDoesNotExistException extends RuntimeException {

    public AppointmentDoesNotExistException() {
        super(ErrorMessages.ACCOUNT_INCOMPLETE.getErrorMessage());
    }

    public AppointmentDoesNotExistException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
