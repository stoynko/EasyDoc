package com.github.stoynko.easydoc.report.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class ReportDoesNotExistException extends RuntimeException {

    public ReportDoesNotExistException() {
        super(ErrorMessages.REPORT_NOT_FOUND.getErrorMessage());
    }
}
