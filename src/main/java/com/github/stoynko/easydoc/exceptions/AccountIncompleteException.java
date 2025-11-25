package com.github.stoynko.easydoc.exceptions;

public class AccountIncompleteException extends RuntimeException {

    public AccountIncompleteException() {
        super(ErrorMessages.ACCOUNT_INCOMPLETE.getErrorMessage());
    }

    public AccountIncompleteException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
