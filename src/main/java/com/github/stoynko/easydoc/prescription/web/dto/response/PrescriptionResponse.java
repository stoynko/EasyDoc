package com.github.stoynko.easydoc.prescription.web.dto.response;

import com.github.stoynko.easydoc.prescription.model.PrescriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class PrescriptionResponse {

    private UUID prescriptionId;

    private UUID appointmentId;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    LocalDateTime issuedAt;

    LocalDateTime expiresAt;

    private PrescriptionStatus prescriptionStatus;

    private List<PrescriptionMedicamentResponse> medicaments;
}
