package com.github.stoynko.easydoc.web.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PrescriptionRequest {

    private UUID appointmentId;

    private List<Object> medicaments;

}
