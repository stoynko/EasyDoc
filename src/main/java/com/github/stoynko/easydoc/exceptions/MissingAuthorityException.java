package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.MISSING_AUTHORITY;

public class MissingAuthorityException extends RuntimeException {

    public MissingAuthorityException() {
        super(MISSING_AUTHORITY.getErrorMessage());
    }

}
