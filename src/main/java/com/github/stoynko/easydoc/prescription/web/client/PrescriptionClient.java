package com.github.stoynko.easydoc.prescription.web.client;

import com.github.stoynko.easydoc.config.PrescriptionFeignConfig;
import com.github.stoynko.easydoc.prescription.web.dto.request.AddMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "prescription-svc", url = "http://localhost:8081/api/v1", configuration = PrescriptionFeignConfig.class)
public interface PrescriptionClient {

    @GetMapping("/appointments/{appointmentId}/prescription")
    ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable UUID appointmentId);

    @PostMapping("/appointments/{appointmentId}/prescription")
    public ResponseEntity<PrescriptionResponse> createPrescription(@PathVariable UUID appointmentId);

    @PostMapping("/prescriptions/{prescriptionId}/medicaments")
    public ResponseEntity<PrescriptionResponse> addMedicament(@PathVariable UUID prescriptionId, @RequestBody AddMedicamentRequest request);

    @DeleteMapping("/prescriptions/{prescriptionId}/medicaments/{medicamentId}")
    public ResponseEntity<PrescriptionResponse> removeMedicament(@PathVariable UUID prescriptionId, @PathVariable UUID medicamentId);

    @PutMapping("/prescriptions/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> issuePrescription(@PathVariable UUID prescriptionId);
}
