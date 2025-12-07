package com.github.stoynko.easydoc.user.web.dto.request;

import com.github.stoynko.easydoc.user.validation.annotation.ValidBirthDate;
import com.github.stoynko.easydoc.user.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitAccountDetailsRequest {

    @NotBlank(message = "Please enter your first name")
    @Size(max = 100, message = "Please enter at most 100 characters")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Size(max = 100, message = "Please enter at most 15 characters")
    private String lastName;

    @NotBlank(message = "Please enter your personal identification number")
    @Size(max = 10, message = "Please enter at most 10 characters")
    @Pattern(regexp = "\\d+", message = "PIN must contain digits only")
    private String pin;

    @ValidBirthDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Please enter your date of birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please select your gender")
    private Gender gender;
}
