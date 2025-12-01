package com.github.stoynko.easydoc.appointment.model;

public enum AppointmentReason {

    PRIMARY("Primary examination"),
    SECONDARY("Follow-up examination"),
    PREVENTATIVE("Preventive examination"),
    DISPENSARY("Dispensary patient"),
    ISSUE_CERTIFICATE("Issuing a medical certificate"),
    ISSUE_PRESCRIPTION(" Issuing a prescription"),
    PROCEDURE("Medical procedure"),
    OTHER_DIAGNOSTIC("Other diagnostics"),
    OTHER("Other");

    private final String displayValue;

    AppointmentReason(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
