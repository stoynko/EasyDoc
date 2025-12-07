package com.github.stoynko.easydoc.web.utilities;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.model.ViewAction;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;
import static com.github.stoynko.easydoc.web.model.ViewAction.READ;
import static com.github.stoynko.easydoc.web.model.ViewAction.WRITE;

@UtilityClass
public class ValidationUtilities {

    public static ViewAction getReportActionFor(Appointment appointment) {

        if (!appointment.hasReport() || appointment.getReport() == null) {
            return WRITE;
        }

        boolean isDraft = appointment.getReport().getReportStatus() == DRAFT;

        return isDraft ? WRITE : READ;
    }
}
