package com.github.stoynko.easydoc.shared.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.CREDENTIALS_INVALID;

public class CredentialsException extends RuntimeException
{
    public CredentialsException() {
        super(CREDENTIALS_INVALID.getErrorMessage());
    }
}
