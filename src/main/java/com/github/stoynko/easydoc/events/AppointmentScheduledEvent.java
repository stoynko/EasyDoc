package com.github.stoynko.easydoc.events;

import com.github.stoynko.easydoc.models.Doctor;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentScheduledEvent(LocalDate date, LocalTime time, Doctor doctor) { }
