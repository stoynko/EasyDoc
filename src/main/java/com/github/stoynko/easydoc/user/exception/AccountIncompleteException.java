package com.github.stoynko.easydoc.user.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class AccountIncompleteException extends RuntimeException {

    public AccountIncompleteException() {
        super(ErrorMessages.ACCOUNT_INCOMPLETE.getErrorMessage());
    }

}
