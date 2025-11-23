package com.github.stoynko.easydoc.exceptions;

public class UserExistsWithEmailException extends RuntimeException {

    public UserExistsWithEmailException(ErrorMessages errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
