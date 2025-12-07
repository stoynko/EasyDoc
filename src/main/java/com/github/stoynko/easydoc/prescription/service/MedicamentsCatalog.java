package com.github.stoynko.easydoc.prescription.service;

import com.github.stoynko.easydoc.prescription.model.DeliveryMethod;
import com.github.stoynko.easydoc.prescription.model.MedicamentType;
import com.github.stoynko.easydoc.prescription.web.client.MedicamentsClient;
import com.github.stoynko.easydoc.prescription.web.dto.response.MedicamentItemResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicamentsCatalog {

    private final MedicamentsClient client;

    public List<MedicamentItemResponse> getFilteredMedicamentsCatalog(MedicamentType medicamentType) {
        List<MedicamentItemResponse> catalog = getMedicamentsCatalog();

        return catalog.stream().filter(m -> m.getType() == medicamentType).toList();
    }

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

    public Set<DeliveryMethod> getFilteredByDeliveryMethod(List<MedicamentItemResponse> filteredMedicaments) {
        return filteredMedicaments.stream()
                .flatMap(m -> m.getDeliveryMethods().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<DeliveryMethod> getDeliveryMethods(UUID medicamentId) {
        List<MedicamentItemResponse> allMedicaments = getMedicamentsCatalog();

        MedicamentItemResponse filteredMedicament = allMedicaments.stream()
                .filter(medicament -> medicament.getMedicamentId().equals(medicamentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown medicament: " + medicamentId));

        List<DeliveryMethod> methods = new ArrayList<>(filteredMedicament.getDeliveryMethods());
        methods.sort(Comparator.comparing(Enum::name));
        return methods;
    }
}
