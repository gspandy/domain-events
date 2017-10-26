package de.audibene.core.domain.events.azure.subject;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import de.audibene.core.domain.events.azure.handler.AzureMessageHandler;
import de.audibene.core.domain.events.azure.support.Memoizers;
import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.converter.Converter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AzureTopicSubject<T> implements Subject<T> {

    private final Converter<T> converter;
    private final Supplier<TopicClient> topic;
    private final Function<String, AzureMessageHandler<T>> handlers;

    public AzureTopicSubject(
            final Converter<T> converter,
            final Supplier<TopicClient> topic,
            final Function<String, AzureMessageHandler<T>> handlers
    ) {

        this.converter = converter;
        this.topic = Memoizers.memoize(topic);
        this.handlers = Memoizers.memoize(handlers);
    }

    @Override
    public void publish(final T event) {
        final TopicClient topicClient = topic.get();
        try {
            final String content = converter.print(event);
            topicClient.send(new Message(content, "application/json"));
        } catch (InterruptedException | ServiceBusException e) {
            throw new IllegalStateException("Can not send message", e);
        }
    }

    @Override
    public void subscribe(final String group, final Consumer<T> listener) {
        handlers.apply(group).subscribe(listener);
    }

}
