package de.audibene.core.domain.events.listener.support;

import de.audibene.core.domain.events.listener.DomainEvent;
import de.audibene.core.domain.events.listener.DomainEventListener;
import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.SubjectProvider;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

import static de.audibene.core.domain.events.listener.support.Methods.hasAnnotation;
import static de.audibene.core.domain.events.listener.support.Methods.hasSingleParameter;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class DomainEventListenerFactory implements EventListenerFactory {

    private final SubjectProvider subjectProvider;
    private final Environment environment;

    public DomainEventListenerFactory(
            final SubjectProvider subjectProvider,
            final Environment environment
    ) {
        this.subjectProvider = subjectProvider;
        this.environment = environment;
    }

    @Override
    public boolean supportsMethod(final Method method) {
        return hasAnnotation(method, DomainEventListener.class) && hasSingleParameter(method, DomainEvent.class);
    }

    @Override
    public ApplicationListener<?> createApplicationListener(
            final String beanName,
            final Class<?> type,
            final Method method
    ) {
        return new DomainEventListenerMethodAdapter(beanName, type, method, environment, subjectProvider);
    }

    @EventListener
    protected final void scheduleInternal(final DomainEvent event) {
        final Class<? extends DomainEvent> eventType = event.getClass();
        final Subject<DomainEvent> subject = subjectProvider.resolve(eventType);
        subject.publish(event);
    }

}
