package de.audibene.core.domain.events.listener.support;

import de.audibene.core.domain.events.listener.DomainEvent;
import de.audibene.core.domain.events.listener.DomainEventListener;
import de.audibene.core.domain.events.listener.TestDomainEvent;
import de.audibene.core.domain.events.subject.SubjectProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class DomainEventListenerFactoryTest {

    @Mock
    private SubjectProvider subjectProvider;

    @InjectMocks
    private DomainEventListenerFactory factory;

    @Test
    public void shouldSupportValidMethod() throws Exception {
        final Method method = Helper.class.getMethod("handleValid", DomainEvent.class);
        assertThat(factory.supportsMethod(method)).isTrue();
    }

    @Test
    public void shouldNotSupportMethodWithWrongType() throws Exception {
        final Method method = Helper.class.getMethod("handleWrongType", ApplicationEvent.class);
        assertThat(factory.supportsMethod(method)).isFalse();
    }

    @Test
    public void shouldNotSupportMethodWithoutAnnotation() throws Exception {
        final Method method = Helper.class.getMethod("handleWithoutAnnotation", DomainEvent.class);
        assertThat(factory.supportsMethod(method)).isFalse();
    }

    @Test
    public void shouldNotSupportMethodWithWrongAnnotation() throws Exception {
        final Method method = Helper.class.getMethod("handleWithWrongAnnotation", DomainEvent.class);
        assertThat(factory.supportsMethod(method)).isFalse();
    }

    @Test
    public void shouldPublishDomainEventInternally() throws Exception {
//        final TestDomainEvent event = new TestDomainEvent();
//        factory.scheduleInternal(event); TODO
    }

    interface Helper {
        @DomainEventListener
        void handleValid(DomainEvent event);
        void handleWrongType(ApplicationEvent event);
        void handleWithoutAnnotation(DomainEvent event);
        @EventListener
        void handleWithWrongAnnotation(DomainEvent event);
    }
}