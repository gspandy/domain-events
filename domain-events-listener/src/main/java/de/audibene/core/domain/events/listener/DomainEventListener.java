package de.audibene.core.domain.events.listener;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@EventListener
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
public @interface DomainEventListener {

    String group() default "";

    @AliasFor(attribute = "classes", annotation = EventListener.class)
    Class<? extends DomainEvent>[] classes() default {};

    @AliasFor(attribute = "condition", annotation = EventListener.class)
    String condition() default "";

}
