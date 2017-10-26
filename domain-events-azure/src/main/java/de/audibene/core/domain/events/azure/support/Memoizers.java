package de.audibene.core.domain.events.azure.support;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Memoizers {

    private Memoizers() {
    }

    @Nonnull
    public static <E> Supplier<E> memoize(final Supplier<E> delegate) {
        final Map<String, E> client = new ConcurrentHashMap<>();
        return () -> client.computeIfAbsent("instance", $ -> delegate.get());
    }

    @Nonnull
    public static <A, E> Function<A, E> memoize(final Function<A, E> delegate) {
        final Map<A, E> cache = new ConcurrentHashMap<>();
        return a -> cache.computeIfAbsent(a, delegate);
    }

    @Nonnull
    public static <A, B, E> BiFunction<A, B, E> memoize(final BiFunction<A, B, E> delegate) {
        final Function<A, Function<B, E>> memoize = memoize(a -> memoize(b -> delegate.apply(a, b)));
        return (a, b) -> memoize.apply(a).apply(b);
    }

}
