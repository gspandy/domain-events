package de.audibene.core.domain.events.subject;

import java.util.function.Consumer;

public interface Subject<T>  {
    void subscribe(String group, Consumer<T> listener);
    void publish(T event);
}
