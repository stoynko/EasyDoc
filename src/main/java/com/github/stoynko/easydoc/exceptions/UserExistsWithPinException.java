package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ACCOUNT_DUPLICATE_PIN;

public class UserExistsWithPinException extends RuntimeException {

    public UserExistsWithPinException() {
        super(ACCOUNT_DUPLICATE_PIN.getErrorMessage());
    }
}
