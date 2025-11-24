package com.github.stoynko.easydoc.web.model;

public enum ViewFragment {

    NONE(""),
    SETTINGS_DASHBOARD("fragments/settings :: dashboard"),
    PERSONAL_INFO("fragments/settings :: personal_info"),
    PROFESSIONAL_INFO("fragments/settings :: professional_info"),
    SECURITY("fragments/settings :: security"),
    PREFERENCES("fragments/settings :: preferences"),
    MEMBERSHIP("fragments/settings :: membership"),
    DELETE_ACCOUNT("fragments/settings :: delete_account"),
    DOCTOR_LANDING("fragments/onboarding :: landing"),
    DOCTOR_ONBOARDING("fragments/onboarding :: onboarding");

    private final String fragmentPath;

    ViewFragment(String fragmentPath) {
        this.fragmentPath = fragmentPath;
    }

    public String getFragmentPath() {
        return fragmentPath;
    }

}
