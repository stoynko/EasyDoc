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
public class UpdateContactDetailsRequest {

    @NotBlank(message = "Please input your phone number")
    private String phoneNumber;
}
