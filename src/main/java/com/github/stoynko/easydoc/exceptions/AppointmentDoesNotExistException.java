package com.github.stoynko.easydoc.exceptions;

public class AppointmentDoesNotExistException extends RuntimeException {

    public AppointmentDoesNotExistException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
