package com.github.stoynko.easydoc.prescription.web.dto.response;

import com.github.stoynko.easydoc.prescription.model.DeliveryMethod;
import com.github.stoynko.easydoc.prescription.model.MedicamentType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class MedicamentItemResponse {

    UUID medicamentId;

    MedicamentType type;

    String description;

    String brandName;

    String genericName;

    Set<DeliveryMethod> deliveryMethods;
}
