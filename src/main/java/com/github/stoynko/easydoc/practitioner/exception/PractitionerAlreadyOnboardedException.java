package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.DOCTOR_ALREADY_ONBOARDED;

public class PractitionerAlreadyOnboardedException extends RuntimeException {

    public PractitionerAlreadyOnboardedException() {
        super(DOCTOR_ALREADY_ONBOARDED.getErrorMessage());
    }
}
