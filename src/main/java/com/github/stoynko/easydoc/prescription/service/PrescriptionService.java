package com.github.stoynko.easydoc.prescription.service;

import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.prescription.web.client.PrescriptionClient;
import com.github.stoynko.easydoc.prescription.web.dto.request.AddMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.request.RemoveMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionClient client;
    private final AppointmentService appointmentService;

    @Transactional
    public void createPrescription(UUID appointmentId) {
        try {
            PrescriptionResponse prescription = client.createPrescription(appointmentId).getBody();
            appointmentService.addPrescriptionToAppointment(appointmentId, prescription.getPrescriptionId());
        } catch (FeignException exception) {
            log.error("[S2S Call] Prescription creation failed due to {}", exception.getMessage());
        }
    }

    @Cacheable(cacheNames = "prescriptions", key = "#appointmentId", unless = "#result == null")
    public PrescriptionResponse getPrescription(UUID appointmentId) {
        try {
            return client.getPrescription(appointmentId).getBody();
        } catch (FeignException.NotFound notFound) {
            return null;
        } catch (FeignException exception) {
            log.error("[S2S Call] Failed due to %s".formatted(exception.getMessage()));
            return null;
        } catch (Exception exception) {
            log.error("Failed to get prescription for appointment with id {}: {}",
                    appointmentId, exception.getMessage(), exception);
            return null;
        }
    }

    @CachePut(value = "prescriptions", key = "#appointmentId")
    public PrescriptionResponse addMedicament(UUID appointmentId, AddMedicamentRequest request) {
        try {
            client.addMedicament(appointmentId, request);
            return client.getPrescription(appointmentId).getBody();
        } catch (FeignException exception) {
            log.error("[S2S Call] Adding medicament to prescription with id {} failed due to {}",
                    request.getPrescriptionId(), exception.getMessage());

            return client.getPrescription(appointmentId).getBody();
        }
    }

    @CachePut(value = "prescriptions", key = "#appointmentId")
    public PrescriptionResponse removeMedicament(UUID appointmentId, RemoveMedicamentRequest request) {
        try {
            client.removeMedicament(appointmentId, request);
            return client.getPrescription(appointmentId).getBody();
        } catch (FeignException exception) {
            log.error("[S2S Call] Removing medicament with id {} from prescription with id {} failed due to {}",
                    request.getMedicamentId(), request.getPrescriptionId(), exception.getMessage());

            return client.getPrescription(appointmentId).getBody();
        }
    }

    @CachePut(value = "prescriptions", key = "#appointmentId")
    public PrescriptionResponse issuePrescription(UUID appointmentId, UUID prescriptionId) {
        try {
            client.issuePrescription(appointmentId, prescriptionId);
            return client.getPrescription(appointmentId).getBody();
        } catch (FeignException exception) {
            log.error("[S2S Call] Issuing prescription with id {} failed due to {}", prescriptionId, exception.getMessage());
            return client.getPrescription(appointmentId).getBody();
        }
    }


}
