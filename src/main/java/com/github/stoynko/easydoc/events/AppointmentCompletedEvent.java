package com.github.stoynko.easydoc.events;

import java.util.UUID;

public record AppointmentCompletedEvent(String emailAddress, UUID appointmentId) {
}
