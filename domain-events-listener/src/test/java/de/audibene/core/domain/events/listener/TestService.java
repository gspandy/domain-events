package de.audibene.core.domain.events.listener;

import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TestService {

    private final BlockingQueue<TestDomainEvent> queue = new LinkedBlockingDeque<>();
    private final ApplicationEventPublisher publisher;

    public TestService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void send() {
        publisher.publishEvent(new TestDomainEvent());
    }

    @DomainEventListener
    public void receive(TestDomainEvent event) {
        queue.offer(event);
    }

    public BlockingQueue<TestDomainEvent> getQueue() {
        return queue;
    }
}
