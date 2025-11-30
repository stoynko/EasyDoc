package com.github.stoynko.easydoc.client.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UpsertPrescriptionRequest {

    private UUID appointmentId;

    private List<Object> medicaments;

}
