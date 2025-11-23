package com.github.stoynko.easydoc.models.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayValue;

    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
