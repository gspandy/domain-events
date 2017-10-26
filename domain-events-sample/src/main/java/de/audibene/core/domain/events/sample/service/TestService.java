package de.audibene.core.domain.events.sample.service;

import de.audibene.core.domain.events.listener.DomainEventListener;
import de.audibene.core.domain.events.sample.event.TestDomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TestService {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public TestService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedRate = 1)
    public void schedule() throws Exception {
        publisher.publishEvent(TestDomainEvent
                .builder()
                .at(Instant.now())
                .domainId(UUID.randomUUID())
                .domainType("test")
                .eventType("created").build());
    }

    @DomainEventListener(group = "${azure.servicebus.subscription-name}")
    public void on(TestDomainEvent event) {
        System.out.println(event);
    }
}
