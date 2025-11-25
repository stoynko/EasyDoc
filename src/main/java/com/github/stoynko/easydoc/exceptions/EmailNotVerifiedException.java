package com.github.stoynko.easydoc.exceptions;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException() {
        super(ErrorMessages.ACCOUNT_UNVERIFIED.getErrorMessage());
    }

    public EmailNotVerifiedException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
