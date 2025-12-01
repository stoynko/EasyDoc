package com.github.stoynko.easydoc.prescription.web.mapper;

import com.github.stoynko.easydoc.prescription.web.dto.UpsertPrescriptionRequest;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.UUID;

@UtilityClass
public class PrescriptionMapper {

    public static UpsertPrescriptionRequest toPrescriptionRequest(UUID appointmentId) {
        return UpsertPrescriptionRequest.builder()
                .appointmentId(appointmentId)
                .medicaments(new ArrayList<>())
                .build();
    }
}
