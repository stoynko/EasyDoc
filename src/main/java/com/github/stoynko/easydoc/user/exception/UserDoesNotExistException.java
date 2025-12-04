package com.github.stoynko.easydoc.user.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.ACCOUNT_NOT_FOUND;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super(ACCOUNT_NOT_FOUND.getErrorMessage());
    }
}
