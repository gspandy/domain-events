package de.audibene.core.domain.events.subject.support;

import de.audibene.core.domain.events.subject.annotation.Topic;
import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ClassesTest {


    @Test
    public void shouldResolveSelfAsAnnotatedBase() throws Exception {
        Class<?> actual = Classes.getAnnotatedBase(Node.class, Topic.class);

        assertThat(actual).isSameAs(Node.class);
    }

    @Test
    public void shouldResolveParentAsAnnotatedBase() throws Exception {
        Class<?> actual = Classes.getAnnotatedBase(Leaf.class, Topic.class);

        assertThat(actual).isSameAs(Node.class);
    }

    @Test
    public void shouldThrowExceptionIfNotAnnotated() throws Exception {
        assertThatThrownBy(() -> Classes.getAnnotatedBase(Map.class, Topic.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Class should be annotated by " + Topic.class);
    }

    @Test
    public void shouldReturnAnnotationFromSelf() throws Exception {
        Topic actual = Classes.getAnnotation(Node.class, Topic.class);

        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldReturnAnnotationFromParent() throws Exception {
        Topic actual = Classes.getAnnotation(Leaf.class, Topic.class);

        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldReturnMetaAnnotation() throws Exception {
        Topic actual = Classes.getAnnotation(Attribute.class, Topic.class);

        assertThat(actual).isNotNull();
    }

    @Topic(name = "node")
    interface Node {
    }

    interface Leaf extends Node {
    }

    @Meta
    interface Attribute {
    }

    @Documented
    @Topic(name = "meta")
    @Retention(RUNTIME)
    @Target({TYPE, ANNOTATION_TYPE})
    public @interface Meta {
    }
}