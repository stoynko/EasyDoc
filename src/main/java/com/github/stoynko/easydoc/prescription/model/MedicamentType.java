package com.github.stoynko.easydoc.prescription.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MedicamentType {

    ANTIBIOTIC("Antibiotic"),
    ANTISPASMODIC("Antispasmodic"),
    PAIN_RELIEF("Pain Relief"),
    ANTIPYRETICS("Antipyretic"),
    ANTIEMETICS("Antiemetic"),
    ANTIDIARRHEALS("Antidiarrheal");

    private String displayValue;

}
