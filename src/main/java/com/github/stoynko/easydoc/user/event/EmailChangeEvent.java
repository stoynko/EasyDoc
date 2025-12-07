package com.github.stoynko.easydoc.user.event;

import java.util.UUID;

public record EmailChangeEvent(UUID userId, String emailAddress, UUID token) {
}
