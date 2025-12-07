package com.github.stoynko.easydoc.user.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException() {
        super(ErrorMessages.ACCOUNT_UNVERIFIED.getErrorMessage());
    }

}
