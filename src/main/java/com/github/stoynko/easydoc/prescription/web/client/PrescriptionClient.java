package com.github.stoynko.easydoc.prescription.web.client;

import com.github.stoynko.easydoc.prescription.web.dto.request.AddMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.request.RemoveMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import com.github.stoynko.easydoc.config.PrescriptionFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "prescription-svc", url = "http://localhost:8081/api/v1", configuration = PrescriptionFeignConfig.class)
public interface PrescriptionClient {

    @GetMapping("/appointments/{appointmentId}/prescription")
    ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable UUID appointmentId);

    @PostMapping("/appointments/{appointmentId}/prescription")
    public ResponseEntity<PrescriptionResponse> createPrescription(@PathVariable UUID appointmentId);

    @PostMapping("/appointments/{appointmentId}/prescription/medicaments")
    public ResponseEntity<PrescriptionResponse> addMedicament(@PathVariable UUID appointmentId,
                                                                @RequestBody AddMedicamentRequest request);

    @PostMapping("appointments/{appointmentId}/prescription/medicaments/remove")
    public ResponseEntity<PrescriptionResponse> removeMedicament(@PathVariable UUID appointmentId,
                                                                 @RequestBody RemoveMedicamentRequest request);

    @PostMapping("appointments/{appointmentId}/prescription/issue")
    public ResponseEntity<PrescriptionResponse> issuePrescription(@PathVariable UUID appointmentId, @RequestBody UUID prescriptionId);
}
