package com.github.stoynko.easydoc.practitioner.web.dto.request;

import com.github.stoynko.easydoc.practitioner.validation.annotation.MaxFileSize;
import com.github.stoynko.easydoc.practitioner.model.Expertise;
import com.github.stoynko.easydoc.user.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPractitionerRequest{

    @NotNull(message = "Please upload a profile photo")
    @MaxFileSize(value = 10 * 1024 * 1024, message = "Profile photo must be at most 20 MB")
    private MultipartFile profilePhoto;

    @NotBlank(message = "Please provide your uin")
    private String uin;

    @NotBlank(message = "Please provide your first name")
    private String firstName;

    @NotBlank(message = "Please provide your last name")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Please provide your date of birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please select your gender")
    private Gender gender;

    @NotNull(message = "Please select your expertise")
    private Expertise expertise;

    @Positive(message = "Please enter a valid range")
    @NotNull(message = "Please your years of experience")
    private int yearsExperience;

    @NotBlank(message = "Please provide the address of your practice")
    private String practiceLocation;

    @NotBlank(message = "Please provide details of your professional highlights")
    @Size(max = 5000, message = "Your professional highlights must be at most 5000 characters.")
    private String professionalHighlights;

    @NotBlank(message = "Please list the languages you speak")
    private String spokenLanguages;
}
