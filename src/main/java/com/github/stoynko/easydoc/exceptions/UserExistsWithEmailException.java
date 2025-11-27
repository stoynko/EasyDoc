package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ACCOUNT_DUPLICATE_EMAIL;

public class UserExistsWithEmailException extends RuntimeException {

    public UserExistsWithEmailException() {
        super(ACCOUNT_DUPLICATE_EMAIL.getErrorMessage());
    }
}
