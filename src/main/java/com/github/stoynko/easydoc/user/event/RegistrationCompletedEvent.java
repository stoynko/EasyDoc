package com.github.stoynko.easydoc.user.event;

import java.util.UUID;

public record RegistrationCompletedEvent(UUID userId, String emailAddress, UUID token) {
}
