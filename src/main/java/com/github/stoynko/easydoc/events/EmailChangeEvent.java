package com.github.stoynko.easydoc.events;

import java.util.UUID;

public record EmailChangeEvent(UUID userId, String emailAddress, UUID token) {
}
