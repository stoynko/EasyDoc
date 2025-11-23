package com.github.stoynko.easydoc.exceptions;

public class UserExistsWithPinException extends RuntimeException {

    public UserExistsWithPinException() {
        super(ErrorMessages.ACCOUNT_DUPLICATE_PIN.getErrorMessage());
    }
}
