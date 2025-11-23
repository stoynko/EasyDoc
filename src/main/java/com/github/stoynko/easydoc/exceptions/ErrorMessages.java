package com.github.stoynko.easydoc.exceptions;

public enum ErrorMessages {

    ACCOUNT_NOT_FOUND("Account does not exist."),
    ACCOUNT_DUPLICATE_EMAIL("User with this email address is already registered."),
    ACCOUNT_DUPLICATE_PIN("User with this personal identification number is already registered."),
    ACCOUNT_VERIFICATION_EXPIRED("Your verification link has expired. Please request a new one."),
    ACCOUNT_SUSPENDED("Your account has been suspended temporarily. Please contact our customer support."),
    ACCOUNT_LOCKED("Your account has been locked due to multiple failed attempts. Try again later or reset your password."),
    INPUT_INVALID("The provided details do not meet the requirements"),
    CREDENTIALS_INVALID("The provided email address and/or password are incorrect."),
    CREDENTIALS_PASSWORD_INVALID("The provided password is incorrect."),

    ALREADY_ONBOARDED("Doctor with these credentials is already registered."),
    DOCTOR_NOT_FOUND("Doctor with such details does not exist."),

    APPOINTMENT_NOT_FOUND("Appointment with such details does not exist"),
    APPOINTMENT_DATE_INVALID("The date for the appointment cannot be in the past"),
    APPOINTMENT_TIME_OUTSIDE_HOURS("The time for the appointment cannot be outside the working hours"),
    APPOINTMENT_TIME_INVALID("The date for the appointment cannot be in the past");


    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}