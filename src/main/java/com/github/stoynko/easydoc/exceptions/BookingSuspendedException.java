package com.github.stoynko.easydoc.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class BookingSuspendedException extends AccessDeniedException {

    public BookingSuspendedException() {
        super(ErrorMessages.BOOKING_SUSPENDED.getErrorMessage());
    }

}
