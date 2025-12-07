package com.github.stoynko.easydoc.user.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "Please enter your email address")
    private String emailAddress;

    @NotNull(message = "Please enter your password.")
    private String password;

}
