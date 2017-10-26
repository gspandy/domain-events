package de.audibene.core.domain.events.subject.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class ObjectMapperConverter<T> implements Converter<T> {
    private final ObjectMapper mapper;
    private final Class<T> target;

    public ObjectMapperConverter(final Class<T> target, final ObjectMapper mapper) {
        this.mapper = mapper;
        this.target = target;
    }

    public ObjectMapperConverter(final Class<T> target) {
        this(target, defaultMapper());
    }

    @Nonnull
    private static ObjectMapper defaultMapper() {
        return new ObjectMapper()
                .registerModules(new JavaTimeModule(), new Jdk8Module())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Nullable
    @Override
    public String print(@Nullable final T value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not print value", e);
        }
    }

    @Nullable
    @Override
    public T parse(@Nullable final String value) {
        try {
            return mapper.readValue(value, target);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not parse value", e);
        }
    }
}
