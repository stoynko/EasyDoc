package com.github.stoynko.easydoc.models.enums;

public enum Expertise {

    SURGEON("Surgeon"),
    ORTHOPEDIST("Orthopedist"),
    UROLOGIST("Urologist"),
    CARDIOLOGIST("Cardiologist"),
    GASTROENTEROLOGIST("Gastroenterologist"),
    PEDIATRICIAN("Pediatrician"),
    ENT("ENT Specialist");

    private final String displayValue;

    Expertise(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
