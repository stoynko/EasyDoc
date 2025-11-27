package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ACCOUNT_NOT_FOUND;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super(ACCOUNT_NOT_FOUND.getErrorMessage());
    }
}
