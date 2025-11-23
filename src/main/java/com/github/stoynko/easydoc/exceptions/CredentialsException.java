package com.github.stoynko.easydoc.exceptions;

public class CredentialsException extends RuntimeException
{
    public CredentialsException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
