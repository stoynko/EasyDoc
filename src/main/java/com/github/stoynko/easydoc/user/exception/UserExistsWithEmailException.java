package com.github.stoynko.easydoc.user.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.ACCOUNT_DUPLICATE_EMAIL;

public class UserExistsWithEmailException extends RuntimeException {

    public UserExistsWithEmailException() {
        super(ACCOUNT_DUPLICATE_EMAIL.getErrorMessage());
    }
}
