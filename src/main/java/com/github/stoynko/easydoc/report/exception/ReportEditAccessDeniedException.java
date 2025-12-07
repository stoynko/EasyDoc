package com.github.stoynko.easydoc.report.exception;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;

public class ReportEditAccessDeniedException extends RuntimeException {

    public ReportEditAccessDeniedException() {
        super(ErrorMessages.REPORT_EDIT_ACCESS_DENIED.getErrorMessage());
    }
}
