package com.github.stoynko.easydoc.prescription.exception;

public enum PrescriptionErrorMessages {

    PRESCRIPTION_EXISTS("Prescription for this appointment already exists"),
    PRESCRIPTION_INVALID_STATUS("The prescription is already issued and no further changes are possible");

    private String displayName;

    PrescriptionErrorMessages(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
