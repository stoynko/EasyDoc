package com.github.stoynko.easydoc.prescription.web.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CreatePrescriptionRequest {

    private UUID appointmentId;

}
