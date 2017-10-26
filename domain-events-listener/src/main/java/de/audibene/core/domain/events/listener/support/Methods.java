package de.audibene.core.domain.events.listener.support;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Methods {
    private Methods() {
    }

    public static <A extends Annotation> A getAnnotation(final Method method, final Class<A> target) {
        A annotation = AnnotatedElementUtils.findMergedAnnotation(method, target);
        if (annotation == null) {
            throw new IllegalStateException("Method should be annotated by " + target);
        } else {
            return annotation;
        }
    }

    public static <T> Class<? extends T> getSingleParameterType(final Method method, final Class<T> target) {
        final int length = method.getParameterTypes().length;
        if (length > 1) {
            throw new IllegalStateException("Maximum one parameter is allowed for event listener method: " + method);
        } else if (length == 0) {
            throw new IllegalStateException("Event parameter is mandatory for event listener method: " + method);
        } else {
            return method.getParameters()[0].getType().asSubclass(target);
        }
    }

    public static <T> boolean hasSingleParameter(final Method method, final Class<T> target) {
        return method.getParameters().length == 1 && target.isAssignableFrom(method.getParameters()[0].getType());
    }

    public static <A extends Annotation> boolean hasAnnotation(final Method method, final Class<A> target) {
        return AnnotationUtils.findAnnotation(method, target) != null;
    }
}
