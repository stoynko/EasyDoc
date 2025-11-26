package com.github.stoynko.easydoc.exceptions;

public class ReportDoesNotExistException extends RuntimeException {

    public ReportDoesNotExistException() {
        super(ErrorMessages.REPORT_NOT_FOUND.getErrorMessage());
    }
}
