package com.github.stoynko.easydoc.user.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Please enter your email address")
    @Email(message = "Please enter a valid email address")
    @Size(max = 100, message = "The email address must be at most 100 characters")
    private String emailAddress;

    @NotBlank (message = "Please enter your password")
    @Size(min = 3, max = 50, message = "The password must be 3-50 characters")
    private String password;

}
