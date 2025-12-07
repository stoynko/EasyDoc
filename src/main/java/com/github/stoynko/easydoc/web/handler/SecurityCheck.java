package com.github.stoynko.easydoc.web.handler;

import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.report.service.ReportService;
import com.github.stoynko.easydoc.user.exception.AccountIncompleteException;
import com.github.stoynko.easydoc.user.exception.AccountSuspendedException;
import com.github.stoynko.easydoc.practitioner.exception.PractitionerApplicationAlreadyExistsException;
import com.github.stoynko.easydoc.appointment.exception.BookingSuspendedException;
import com.github.stoynko.easydoc.user.exception.EmailNotVerifiedException;
import com.github.stoynko.easydoc.shared.exception.MissingAuthorityException;
import com.github.stoynko.easydoc.user.model.AccountAuthority;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
import com.github.stoynko.easydoc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import static com.github.stoynko.easydoc.user.model.AccountAuthority.CAN_BOOK_APPOINTMENT;
import static com.github.stoynko.easydoc.user.model.AccountAuthority.CAN_SUBMIT_PRACTITIONER_APPLICATION;

@Component(value = "securityCheck")
@RequiredArgsConstructor
public class SecurityCheck {

    private final PractitionerApplicationService practitionerApplicationService;

    public boolean isAccountActive(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        switch (principal.getAccountStatus()) {
            case EMAIL_UNVERIFIED -> throw new EmailNotVerifiedException();
            case INCOMPLETE -> throw new AccountIncompleteException();
            case SUSPENDED -> throw new AccountSuspendedException();
        }

        return true;
    }

    public boolean hasAuthority(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                AccountAuthority authority) {

        boolean hasAuthority = principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority() == authority.name());

        if (!hasAuthority) {
            throw new MissingAuthorityException();
        }

        return true;
    }

    public boolean canBookAppointment(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        boolean isAccountActive = isAccountActive(principal);
        boolean hasAuthority;
        try {
            hasAuthority = hasAuthority(principal, CAN_BOOK_APPOINTMENT);
        } catch (MissingAuthorityException exception) {
            throw new BookingSuspendedException();
        }
        return isAccountActive && hasAuthority;
    }

    public boolean canSubmitPractitionerApplication(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        boolean hasPendingApplication = practitionerApplicationService.hasPendingApplication(principal.getId());
        boolean hasApprovedApplication = practitionerApplicationService.hasApprovedApplication(principal.getId());

        if (hasPendingApplication || hasApprovedApplication) {
            throw new PractitionerApplicationAlreadyExistsException();
        }

        return isAccountActive(principal) && hasAuthority(principal, CAN_SUBMIT_PRACTITIONER_APPLICATION);
    }

}