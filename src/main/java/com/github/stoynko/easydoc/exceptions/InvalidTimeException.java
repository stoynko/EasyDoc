package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_TIME_INVALID;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException() {
        super(APPOINTMENT_TIME_INVALID.getErrorMessage());
    }
}
