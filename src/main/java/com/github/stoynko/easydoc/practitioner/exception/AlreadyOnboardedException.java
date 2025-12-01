package com.github.stoynko.easydoc.practitioner.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.ALREADY_ONBOARDED;

public class AlreadyOnboardedException extends RuntimeException {

    public AlreadyOnboardedException() {
        super(ALREADY_ONBOARDED.getErrorMessage());
    }
}
