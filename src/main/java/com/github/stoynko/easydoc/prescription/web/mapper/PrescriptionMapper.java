package com.github.stoynko.easydoc.prescription.web.mapper;

import com.github.stoynko.easydoc.prescription.web.dto.request.CreatePrescriptionRequest;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class PrescriptionMapper {

    public static CreatePrescriptionRequest toPrescriptionRequest(UUID appointmentId) {
        return CreatePrescriptionRequest.builder()
                .appointmentId(appointmentId)
                .build();
    }

}
