package com.github.stoynko.easydoc.prescription.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class RemoveMedicamentRequest {

    private UUID prescriptionId;

    private UUID medicamentId;
}
