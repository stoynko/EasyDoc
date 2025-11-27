package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ALREADY_ONBOARDED;

public class AlreadyOnboardedException extends RuntimeException {

    public AlreadyOnboardedException() {
        super(ALREADY_ONBOARDED.getErrorMessage());
    }
}
