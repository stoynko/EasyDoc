package com.github.stoynko.easydoc.user.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteAccountRequest {

    @Pattern(regexp = "^I confirm the deletion of my account$", message = "Please enter the exact confirmation to proceed")
    private String confirmationInput;

    @NotBlank(message = "Please confirm the action with your current password")
    private String passwordConfirmation;
}
