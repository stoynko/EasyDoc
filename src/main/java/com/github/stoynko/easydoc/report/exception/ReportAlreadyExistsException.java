package com.github.stoynko.easydoc.report.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class ReportAlreadyExistsException extends RuntimeException {

    public ReportAlreadyExistsException() {
        super(ErrorMessages.REPORT_ALREADY_EXISTS.getErrorMessage());
    }
}
