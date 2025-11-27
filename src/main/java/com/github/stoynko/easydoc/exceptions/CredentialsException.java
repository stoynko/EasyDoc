package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.CREDENTIALS_INVALID;

public class CredentialsException extends RuntimeException
{
    public CredentialsException() {
        super(CREDENTIALS_INVALID.getErrorMessage());
    }
}
