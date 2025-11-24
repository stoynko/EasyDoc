package com.github.stoynko.easydoc.web.model;

public enum ViewPage {

    INDEX("index"),
    REGISTER("register"),
    LOGIN("login"),
    DASHBOARD("dashboard"),
    APPOINTMENTS("appointments"),
    APPOINTMENT("appointment_creation"),
    PRESCRIPTIONS("prescriptions"),
    MEDICAL_REPORT_CREATION("medical_report_creation"),
    MEDICAL_REPORTS("reports"),
    DOCTORS("doctors"),
    ACCOUNT_ONBOARDING("onboarding_account"),
    DOCTOR_ONBOARDING("onboarding_doctors"),
    PRACTITIONER_APPLICATIONS("applications_review"),
    USERS("users"),
    ACCOUNT("account"),
    SETTINGS("settings"),
    NOTIFICATIONS("notifications"),
    MESSAGES("messages"),
    CONFIRMATION("action_confirmation"),
    ERROR("action_error");

    private final String page;

    ViewPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
