package de.audibene.core.domain.events.listener;

import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.SubjectProvider;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TestSubjectProvider implements SubjectProvider {

    private final Subject<?> subject = new FakeSubject<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Subject<T> resolve(Class<? extends T> target) {
        return (Subject<T>) subject;
    }

    private static class FakeSubject<T> implements Subject<T> {
        private final Set<Consumer<T>> listeners = new LinkedHashSet<>();
        private final ScheduledExecutorService executorService = newScheduledThreadPool(10);

        @Override
        public void subscribe(String group, Consumer<T> listener) {
            listeners.add(listener);
        }

        @Override
        public void publish(T event) {
            executorService.schedule(() -> offer(event), 500, MILLISECONDS);
        }

        private void offer(T event) {
            listeners.forEach(listener -> listener.accept(event));
        }
    }
}
