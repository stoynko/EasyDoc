package com.github.stoynko.easydoc.exceptions;

public class AccountSuspendedException extends RuntimeException {

    public AccountSuspendedException() {
        super(ErrorMessages.ACCOUNT_SUSPENDED.getErrorMessage());
    }

    public AccountSuspendedException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
