package com.github.stoynko.easydoc.prescription.web.dto.request;

import com.github.stoynko.easydoc.prescription.model.DeliveryMethod;
import com.github.stoynko.easydoc.prescription.model.MedicamentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMedicamentRequest {

    private UUID medicamentId;

    private UUID prescriptionId;

    @NotNull(message = "Please select one of the following")
    private MedicamentType medicamentType;

    @NotNull(message = "Please select one of the following")
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = "Please enter intake frequency")
    private String intakeFrequency;

    @NotBlank(message = "Please enter intake dosage")
    private String intakeDosage;

    @NotBlank(message = "Please enter the treatment duration")
    private String treatmentDuration;

    private String additionalNotes;
}
