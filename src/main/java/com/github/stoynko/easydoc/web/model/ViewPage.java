package com.github.stoynko.easydoc.web.model;

public enum ViewPage {

    INDEX("pages/index"),
    REGISTER("pages/register"),
    LOGIN("pages/login"),
    DASHBOARD("pages/dashboard"),
    APPOINTMENTS("pages/appointments"),
    APPOINTMENT("pages/appointment"),
    PRESCRIPTIONS("pages/prescriptions"),
    MEDICAL_REPORTS("pages/reports"),
    DOCTORS("pages/doctors"),
    ACCOUNT_ONBOARDING("pages/onboarding-account"),
    DOCTOR_ONBOARDING("pages/onboarding"),
    PRACTITIONER_APPLICATIONS("pages/applications"),
    USERS("pages/users"),
    ACCOUNT("pages/account"),
    SETTINGS("pages/settings"),
    NOTIFICATIONS("pages/notifications"),
    MESSAGES("pages/messages"),
    CONFIRMATION("pages/confirmation"),
    ERROR("pages/errors");

    private final String page;

    ViewPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
