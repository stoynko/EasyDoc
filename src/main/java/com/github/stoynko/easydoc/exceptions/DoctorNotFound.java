package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.DOCTOR_NOT_FOUND;

public class DoctorNotFound extends RuntimeException {

    public DoctorNotFound() {
        super(DOCTOR_NOT_FOUND.getErrorMessage());
    }
}
