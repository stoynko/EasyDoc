package com.github.stoynko.easydoc.report.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class ReportAlreadyIssuedException extends RuntimeException {

    public ReportAlreadyIssuedException() {
        super(ErrorMessages.REPORT_ALREADY_ISSUED.getErrorMessage());
    }
}
