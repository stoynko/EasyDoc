package com.github.stoynko.easydoc.prescription.service;

import com.github.stoynko.easydoc.prescription.web.client.PrescriptionClient;
import com.github.stoynko.easydoc.prescription.web.dto.UpsertPrescriptionRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.github.stoynko.easydoc.prescription.web.mapper.PrescriptionMapper.toPrescriptionRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionClient client;

    public void upsertPrescription(UUID appointmentId) {

        UpsertPrescriptionRequest request = toPrescriptionRequest(appointmentId);

        try {
            client.upsertPrescription(request);
        } catch (FeignException exception) {
            log.error("[S2S Call] Failed due to %s".formatted(exception.getMessage()));
        }
    }
}
