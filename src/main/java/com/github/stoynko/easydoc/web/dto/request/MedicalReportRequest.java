package com.github.stoynko.easydoc.web.dto.request;

import com.github.stoynko.easydoc.models.Appointment;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MedicalReportRequest {

    private UUID appointmentId;

    private String accompanyingIlnesses;

    private String anamnesis;

    private String statusAtExam;

    private String diagnosticProcedures;

    private String recommendations;

    private UUID prescription;
}
