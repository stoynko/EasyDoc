package com.github.stoynko.easydoc.appointment.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.APPOINTMENT_DATE_INVALID;

public class PastDateException extends RuntimeException {

    public PastDateException() {
        super(APPOINTMENT_DATE_INVALID.getErrorMessage());
    }
}
