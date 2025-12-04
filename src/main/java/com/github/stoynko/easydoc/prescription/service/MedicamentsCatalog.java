package com.github.stoynko.easydoc.prescription.service;

import com.github.stoynko.easydoc.prescription.web.client.MedicamentsClient;
import com.github.stoynko.easydoc.prescription.web.dto.response.MedicamentItemResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicamentsCatalog {

    private final MedicamentsClient client;

    @Cacheable("medicaments")
    public List<MedicamentItemResponse> getMedicamentsCatalog() {
        try {
            List<MedicamentItemResponse> medicaments = client.getAllMedicaments().getBody();
            return medicaments != null ? medicaments : List.of();
        } catch (FeignException exception) {
            log.error("[S2S Call] Failed due to %s".formatted(exception.getMessage()));
        }
        return null;
    }
}
