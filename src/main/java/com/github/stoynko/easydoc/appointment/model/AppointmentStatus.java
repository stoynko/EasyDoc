package com.github.stoynko.easydoc.appointment.model;

public enum AppointmentStatus {

    PENDING("Pending"),
    PROCESSING("Processing"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    NO_SHOW("No Show");

    private final String displayValue;

    AppointmentStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
