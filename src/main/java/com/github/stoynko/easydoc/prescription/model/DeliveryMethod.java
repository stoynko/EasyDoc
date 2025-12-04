package com.github.stoynko.easydoc.prescription.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryMethod {
    ORAL("Oral"),
    TOPICAL("Topical"),
    INHALATION("Inhalation"),
    INJECTION("Injection"),
    SUPPOSITORY("Suppository"),
    OTHER("Other");

    private String displayValue;

}
