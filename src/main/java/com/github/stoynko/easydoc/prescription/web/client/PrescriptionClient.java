package com.github.stoynko.easydoc.prescription.web.client;

import com.github.stoynko.easydoc.prescription.web.dto.PrescriptionResponse;
import com.github.stoynko.easydoc.prescription.web.dto.UpsertPrescriptionRequest;
import com.github.stoynko.easydoc.config.PrescriptionFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "prescription-svc", url = "http://localhost:8081/api/v1", configuration = PrescriptionFeignConfig.class)
public interface PrescriptionClient {

    @GetMapping("/prescriptions")
    ResponseEntity<PrescriptionResponse> getPrescription(@RequestParam UUID appointmentId);

    @PostMapping("/prescriptions")
    ResponseEntity<Void> upsertPrescription(@RequestBody UpsertPrescriptionRequest requestBody);
}
