package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_DATE_INVALID;

public class PastDateException extends RuntimeException {

    public PastDateException() {
        super(APPOINTMENT_DATE_INVALID.getErrorMessage());
    }
}
