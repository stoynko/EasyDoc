package com.github.stoynko.easydoc.prescription.web.client;

import com.github.stoynko.easydoc.prescription.web.dto.response.MedicamentItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "medicaments-client", url = "http://localhost:8081/api/v1")
public interface MedicamentsClient {

    @GetMapping("/medicaments")
    ResponseEntity<List<MedicamentItemResponse>> getAllMedicaments();

}
