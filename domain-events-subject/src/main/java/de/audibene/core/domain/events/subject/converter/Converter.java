package de.audibene.core.domain.events.subject.converter;

public interface Converter<T> {
    String print(T value);
    T parse(String value);
}
