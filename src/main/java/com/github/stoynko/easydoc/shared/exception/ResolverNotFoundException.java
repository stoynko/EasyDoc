package com.github.stoynko.easydoc.shared.exception;

public class ResolverNotFoundException extends RuntimeException {

    public ResolverNotFoundException() {
        super(ErrorMessages.RESOLVER_NOT_FOUND.getErrorMessage());
    }
}
