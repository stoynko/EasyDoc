package com.github.stoynko.easydoc.appointment.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.APPOINTMENT_TIME_INVALID;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException() {
        super(APPOINTMENT_TIME_INVALID.getErrorMessage());
    }
}
