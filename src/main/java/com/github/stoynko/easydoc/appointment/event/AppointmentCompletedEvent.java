package com.github.stoynko.easydoc.appointment.event;

import java.util.UUID;

public record AppointmentCompletedEvent(String emailAddress, UUID appointmentId) {
}
