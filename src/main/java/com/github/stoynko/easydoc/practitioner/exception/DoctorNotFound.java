package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.DOCTOR_NOT_FOUND;

public class DoctorNotFound extends RuntimeException {

    public DoctorNotFound() {
        super(DOCTOR_NOT_FOUND.getErrorMessage());
    }
}
