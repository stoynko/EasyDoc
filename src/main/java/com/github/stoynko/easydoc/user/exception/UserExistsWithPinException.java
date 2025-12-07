package com.github.stoynko.easydoc.user.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.ACCOUNT_DUPLICATE_PIN;

public class UserExistsWithPinException extends RuntimeException {

    public UserExistsWithPinException() {
        super(ACCOUNT_DUPLICATE_PIN.getErrorMessage());
    }
}
