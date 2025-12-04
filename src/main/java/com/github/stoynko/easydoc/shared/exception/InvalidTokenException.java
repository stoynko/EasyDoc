package com.github.stoynko.easydoc.shared.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.INVALID_TOKEN;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super(INVALID_TOKEN.getErrorMessage());
    }
}
