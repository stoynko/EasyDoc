package com.github.stoynko.easydoc.appointment.web.dto.request;

import com.github.stoynko.easydoc.appointment.model.AppointmentReason;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Please choose a suitable date for your appointment")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull(message = "Please choose a suitable time for your appointment")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @NotNull(message = "Please select the reason for your visit")
    private AppointmentReason appointmentReason;

    @Size(max = 1000, message = "The additional details must be at most 1000 characters")
    private String additionalNotes;
}
