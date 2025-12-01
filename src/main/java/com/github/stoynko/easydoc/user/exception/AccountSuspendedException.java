package com.github.stoynko.easydoc.user.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class AccountSuspendedException extends RuntimeException {

    public AccountSuspendedException() {
        super(ErrorMessages.ACCOUNT_SUSPENDED.getErrorMessage());
    }

}
