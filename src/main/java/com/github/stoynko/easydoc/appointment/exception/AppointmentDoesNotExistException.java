package com.github.stoynko.easydoc.appointment.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.APPOINTMENT_NOT_FOUND;

public class AppointmentDoesNotExistException extends RuntimeException {

    public AppointmentDoesNotExistException() {
        super(APPOINTMENT_NOT_FOUND.getErrorMessage());
    }
}
