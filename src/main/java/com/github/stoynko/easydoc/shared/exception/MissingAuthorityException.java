package com.github.stoynko.easydoc.shared.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.MISSING_AUTHORITY;

public class MissingAuthorityException extends RuntimeException {

    public MissingAuthorityException() {
        super(MISSING_AUTHORITY.getErrorMessage());
    }

}
