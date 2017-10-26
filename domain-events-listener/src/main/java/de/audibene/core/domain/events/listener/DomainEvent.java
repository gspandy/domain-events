package de.audibene.core.domain.events.listener;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.time.Instant;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "domainType"
)
public interface DomainEvent<ID, DT, ET> {
    Instant getAt();
    ID getDomainId();
    ET getEventType();
    DT getDomainType();
}
