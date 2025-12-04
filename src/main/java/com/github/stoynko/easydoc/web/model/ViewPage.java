package com.github.stoynko.easydoc.web.model;

public enum ViewPage {

    INDEX("index"),
    REGISTER("register"),
    LOGIN("login"),
    DASHBOARD("dashboard"),
    APPOINTMENTS_TABLE("appointments_table"),
    APPOINTMENT_CREATION("appointment_creation"),
    MEDICAL_REPORT_VIEW("report_review"),
    MEDICAL_REPORTS_TABLE("reports_table"),
    PRESCRIPTIONS_TABLE("prescriptions_table"),
    PRESCRIPTION_VIEW("prescription_review"),
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
