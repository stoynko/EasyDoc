package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_NOT_FOUND;

public class AppointmentDoesNotExistException extends RuntimeException {

    public AppointmentDoesNotExistException() {
        super(APPOINTMENT_NOT_FOUND.getErrorMessage());
    }
}
