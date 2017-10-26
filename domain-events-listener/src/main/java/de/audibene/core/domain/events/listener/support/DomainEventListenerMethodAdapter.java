package de.audibene.core.domain.events.listener.support;

import de.audibene.core.domain.events.listener.DomainEvent;
import de.audibene.core.domain.events.listener.DomainEventListener;
import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.SubjectProvider;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static de.audibene.core.domain.events.listener.support.Methods.getAnnotation;
import static de.audibene.core.domain.events.listener.support.Methods.getSingleParameterType;

public class DomainEventListenerMethodAdapter extends ApplicationListenerMethodAdapter {

    public DomainEventListenerMethodAdapter(
            final String beanName,
            final Class<?> targetClass,
            final Method method,
            final Environment environment,
            final SubjectProvider subjectProvider
            ) {
        super(beanName, targetClass, method);
        initializeListeners(method, environment, subjectProvider);
    }

    @Override
    public boolean supportsEventType(final ResolvableType eventType) {
        return false;
    }

    @Override
    public void onApplicationEvent(final ApplicationEvent event) { }

    private void initializeListeners(final Method method, Environment environment,
                                     final SubjectProvider subjectProvider) {
        final Consumer<DomainEvent> consumer = event -> processEvent(new PayloadApplicationEvent<>(this, event));
        final List<Class<? extends DomainEvent>> eventTypes = resolveDeclaredEventTypes(method);
        final DomainEventListener annotation = getAnnotation(method, DomainEventListener.class);
        final String groupName = environment.resolvePlaceholders(annotation.group());

        eventTypes.forEach(eventType -> {
            final Subject<DomainEvent> subject = subjectProvider.resolve(eventType);
            subject.subscribe(groupName, consumer);
        });
    }

    private List<Class<? extends DomainEvent>> resolveDeclaredEventTypes(final Method method) {
        final DomainEventListener annotation = getAnnotation(method, DomainEventListener.class);
        final Class<? extends DomainEvent> parameterType = getSingleParameterType(method, DomainEvent.class);
        return annotation.classes().length > 0 ?
                Arrays.asList(annotation.classes()) :
                Collections.singletonList(parameterType);
    }

}
