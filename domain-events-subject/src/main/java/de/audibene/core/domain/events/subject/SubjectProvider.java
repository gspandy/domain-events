package de.audibene.core.domain.events.subject;

public interface SubjectProvider {
    <T> Subject<T> resolve(Class<? extends T> target);
}
