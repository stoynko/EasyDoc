package com.github.stoynko.easydoc.user.web.dto.request;

import com.github.stoynko.easydoc.user.validation.annotation.ValidBirthDate;
import com.github.stoynko.easydoc.user.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDetailsRequest {

    @NotBlank(message = "Please enter your first name")
    @Size(max = 100, message = "Please enter at most 100 characters")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Size(max = 100, message = "Please enter at most 100 characters")
    private String lastName;

    @ValidBirthDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Please enter your date of birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please select your gender")
    private Gender gender;
}
