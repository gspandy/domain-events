package de.audibene.core.domain.events.subject.converter;

public class Converters {

    private Converters() {
    }

    public static <T> Converter<T> defaultConverter(final Class<T> target) {
        return new ObjectMapperConverter<>(target);
    }
}
