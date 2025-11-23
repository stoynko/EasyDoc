package com.github.stoynko.easydoc.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super(ErrorMessages.ACCOUNT_NOT_FOUND.getErrorMessage());
    }
}
