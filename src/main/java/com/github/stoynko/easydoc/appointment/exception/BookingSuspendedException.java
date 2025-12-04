package com.github.stoynko.easydoc.appointment.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;
import org.springframework.security.access.AccessDeniedException;

public class BookingSuspendedException extends AccessDeniedException {

    public BookingSuspendedException() {
        super(ErrorMessages.BOOKING_SUSPENDED.getErrorMessage());
    }

}
