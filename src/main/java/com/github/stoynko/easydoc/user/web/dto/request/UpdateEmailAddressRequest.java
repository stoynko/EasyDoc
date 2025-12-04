package com.github.stoynko.easydoc.user.web.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmailAddressRequest {

    @Email
    @NotBlank
    private String currentEmailAddress;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Please enter your new email address")
    private String newEmailAddress;

    @NotBlank(message = "Please confirm the change with your current password")
    private String password;

    @AssertTrue(message = "New email address must be different from current email address")
    public boolean isNewEmailDifferent() {
        return !currentEmailAddress.trim().equalsIgnoreCase(newEmailAddress.trim());
    }
}
