package com.github.stoynko.easydoc.appointment.event;

import com.github.stoynko.easydoc.practitioner.model.Doctor;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentCancelledEvent(LocalDate date, LocalTime time, Doctor doctor) {
}
