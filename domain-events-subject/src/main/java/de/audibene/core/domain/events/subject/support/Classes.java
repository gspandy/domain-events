package de.audibene.core.domain.events.subject.support;

import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;

public class Classes {

    @Nonnull
    public static <A extends Annotation> A getAnnotation(final Class<?> source, final Class<A> target) {
        A annotation = AnnotatedElementUtils.findMergedAnnotation(source, target);
        if (annotation == null) {
            throw new IllegalStateException("Class should be annotated by " + target);
        } else {
            return annotation;
        }
    }


    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T, A extends Annotation> Class<T> getAnnotatedBase(
            final @Nullable Class<?> source,
            final Class<A> target
    ) {
        if (source == null) {
            throw new IllegalStateException("Class should be annotated by " + target);
        }
        if (source.isAnnotationPresent(target)) {
            return (Class<T>) source;
        }
        return (Class<T>) Stream
                .of(source.getInterfaces())
                .filter(s -> s.isAnnotationPresent(target))
                .findFirst()
                .orElseGet(() -> getAnnotatedBase(source.getSuperclass(), target));
    }

}
