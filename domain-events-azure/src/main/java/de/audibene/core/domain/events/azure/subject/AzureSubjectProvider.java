package de.audibene.core.domain.events.azure.subject;

import de.audibene.core.domain.events.azure.properties.AzureSubjectsProperties;
import de.audibene.core.domain.events.azure.support.Factories;
import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.SubjectProvider;
import de.audibene.core.domain.events.subject.annotation.Topic;
import de.audibene.core.domain.events.subject.support.Classes;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;

import static de.audibene.core.domain.events.azure.support.Memoizers.memoize;

public class AzureSubjectProvider implements SubjectProvider {

    private Function<Class<Object>, Class<Object>> classes;
    private Function<Class<Object>, Subject<Object>> subjects;

    public AzureSubjectProvider(
            final AzureSubjectsProperties properties,
            final Environment environment
    ) {

        final BiFunction<String, Class<Object>, Subject<Object>> factory = Factories.subjectFactory(properties);
        this.classes = memoize(target -> Classes.getAnnotatedBase(target, Topic.class));
        this.subjects = memoize(target -> {
            final Topic topic = Classes.getAnnotation(target, Topic.class);
            final String topicName = environment.resolvePlaceholders(topic.name());
            return factory.apply(topicName, target);
        });
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> Subject<T> resolve(final Class<? extends T> target) {
        return (Subject<T>) classes.andThen(subjects).apply(Class.class.cast(target));
    }
}
