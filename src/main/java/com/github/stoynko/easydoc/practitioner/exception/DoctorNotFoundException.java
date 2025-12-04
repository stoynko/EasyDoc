package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.DOCTOR_NOT_FOUND;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException() {
        super(DOCTOR_NOT_FOUND.getErrorMessage());
    }
}
