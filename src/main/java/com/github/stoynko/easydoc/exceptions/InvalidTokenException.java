package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.INVALID_TOKEN;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super(INVALID_TOKEN.getErrorMessage());
    }
}
