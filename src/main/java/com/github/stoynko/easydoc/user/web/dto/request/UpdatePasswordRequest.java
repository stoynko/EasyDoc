package com.github.stoynko.easydoc.user.web.dto.request;

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
public class UpdatePasswordRequest {

    @NotBlank(message = "Please enter your current password")
    private String currentPassword;

    @NotBlank(message = "Please enter your new password")
    private String newPassword;

    @NotBlank(message = "Please confirm your new password")
    private String confirmNewPassword;
}
