package com.github.stoynko.easydoc.events;

import java.util.UUID;

public record RegistrationCompletedEvent(UUID userId, String emailAddress, UUID token) {
}
