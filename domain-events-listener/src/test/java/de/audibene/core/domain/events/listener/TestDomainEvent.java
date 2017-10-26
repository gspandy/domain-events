package de.audibene.core.domain.events.listener;

import java.time.Instant;
import java.util.UUID;

public class TestDomainEvent implements DomainEvent<UUID, String, String> {
    private final Instant at = Instant.now();
    private final UUID domainId = UUID.randomUUID();

    @Override
    public Instant getAt() {
        return at;
    }

    @Override
    public UUID getDomainId() {
        return domainId;
    }

    @Override
    public String getEventType() {
        return "created";
    }

    @Override
    public String getDomainType() {
        return "test";
    }
}
