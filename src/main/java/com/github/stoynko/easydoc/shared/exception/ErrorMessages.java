package com.github.stoynko.easydoc.shared.exception;

public enum ErrorMessages {

    RESOLVER_NOT_FOUND("No resolver was found for user's role"),
    ACCOUNT_NOT_FOUND("Account does not exist."),
    ACCOUNT_DUPLICATE_EMAIL("User with this email address is already registered."),
    ACCOUNT_DUPLICATE_PIN("User with this personal identification number is already registered."),
    ACCOUNT_VERIFICATION_EXPIRED("Your verification link has expired. Please request a new one."),
    ACCOUNT_UNVERIFIED("To unlock the complete functionality of your account, please verify your email address using the link sent to your inbox"),
    ACCOUNT_SUSPENDED("Your account has been suspended temporarily. Please contact our customer support."),
    ACCOUNT_INCOMPLETE("You have not submitted your personal details. Please proceed in doing so via the settings panel to be able to unlock the complete functionality of your account"),
    ACCOUNT_ALREADY_ONBOARDED("This account has already been onboarded"),

    INPUT_INVALID("The provided details do not meet the requirements"),
    CREDENTIALS_INVALID("The provided email address and/or password are incorrect."),
    CREDENTIALS_PASSWORD_INVALID("The provided password is incorrect."),

    DOCTOR_ALREADY_ONBOARDED("Doctor with these credentials is already registered."),
    DOCTOR_NOT_FOUND("Doctor with such details does not exist."),

    PRACTITIONER_APPLICATION_PENDING_EXISTS("An application for practitioner profile has already been submitted."),

    APPOINTMENT_NOT_FOUND("Appointment with such details does not exist"),
    APPOINTMENT_DATE_INVALID("The date for the appointment cannot be in the past"),
    APPOINTMENT_TIME_OUTSIDE_HOURS("The time for the appointment cannot be outside the specified working hours"),
    APPOINTMENT_TIME_INVALID("The date for the appointment cannot be in the past"),

    REPORT_NOT_FOUND("Report with this id does not exist"),
    REPORT_ALREADY_EXISTS("A medical report was already associated with this appointment"),
    REPORT_ALREADY_ISSUED("The medical report has already been issued. No further changes are possible"),
    REPORT_EDIT_ACCESS_DENIED("You do not have authority to edit this report"),

    MISSING_AUTHORITY("You do not have the necessary authority to access this page/resource"),

    BOOKING_SUSPENDED("You are currently unable to schedule appointments. Please try again later and if the issue persists, you are welcome to get in touch with our support team"),

    INVALID_TOKEN("The token is invalid or has expired");

    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}