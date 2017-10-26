package de.audibene.core.domain.events.sample.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.audibene.core.domain.events.listener.DomainEvent;
import de.audibene.core.domain.events.subject.annotation.Topic;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
@JsonTypeName("test")
@Topic(name = "${azure.servicebus.topic-name}")
public class TestDomainEvent implements DomainEvent<UUID, String, String>{
    Instant at;
    UUID domainId;
    String domainType;
    String eventType;
}
