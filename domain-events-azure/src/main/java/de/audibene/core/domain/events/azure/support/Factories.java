package de.audibene.core.domain.events.azure.support;

import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import de.audibene.core.domain.events.azure.handler.AzureMessageHandler;
import de.audibene.core.domain.events.azure.properties.AzureSubjectsProperties;
import de.audibene.core.domain.events.azure.subject.AzureTopicSubject;
import de.audibene.core.domain.events.subject.Subject;
import de.audibene.core.domain.events.subject.SubjectProperties;
import de.audibene.core.domain.events.subject.converter.Converter;
import de.audibene.core.domain.events.subject.converter.Converters;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

import static de.audibene.core.domain.events.azure.support.Memoizers.memoize;

public final class Factories {

    private Factories() {
    }

    @Nonnull
    public static <T> BiFunction<String, Class<T>, Subject<T>> subjectFactory(
            final AzureSubjectsProperties properties
    ) {

        final Function<String, TopicClient> topicsFactory = topicClientFactory(properties);

        return memoize((topicName, target) -> {
            Converter<T> converter = Converters.defaultConverter(target);
            BiFunction<String, String, AzureMessageHandler<T>> handlers = messageHandlerFactory(properties, converter);

            return new AzureTopicSubject<>(
                    converter,
                    () -> topicsFactory.apply(topicName),
                    groupName -> handlers.apply(topicName, groupName)
            );
        });
    }

    private static Function<String, MessageHandlerOptions> subscriptionOptionsFactory(
            final AzureSubjectsProperties properties
    ) {
        return memoize(topicName -> {
            final SubjectProperties subject = properties.getSubject(topicName);
            final int maximumConnections = subject.getMaximumConnections();
            return new MessageHandlerOptions(maximumConnections, true, Duration.ofSeconds(10));
        });
    }

    @Nonnull
    private static Function<String, TopicClient> topicClientFactory(final AzureSubjectsProperties properties) {
        return memoize((topicName) -> {
            try {
                final String connectionString = properties.getConnectionString();
                final ConnectionStringBuilder builder = new ConnectionStringBuilder(connectionString, topicName);
                return new TopicClient(builder);
            } catch (InterruptedException | ServiceBusException e) {
                throw new IllegalStateException("Can not create topic", e);
            }
        });
    }

    @Nonnull
    private static BiFunction<String, String, SubscriptionClient> subscriptionFactory(
            final AzureSubjectsProperties properties
    ) {
        return memoize((topicName, subscriptionName) -> {
            try {
                final String entityPath = String.format("%s/subscriptions/%s", topicName, subscriptionName);
                final String connectionString = properties.getConnectionString();
                final ConnectionStringBuilder builder = new ConnectionStringBuilder(connectionString, entityPath);
                return new SubscriptionClient(builder, ReceiveMode.PEEKLOCK);
            } catch (InterruptedException | ServiceBusException e) {
                throw new IllegalStateException("Can not create subscription", e);
            }
        });
    }

    @Nonnull
    private static Function<String, Executor> executorFactor(
            final AzureSubjectsProperties properties
    ) {
        return memoize(topicName -> {
            final SubjectProperties subject = properties.getSubject(topicName);
            final int processors = subject.getMaximumProcessors();
            return Executors.newFixedThreadPool(processors);
        });
    }

    @Nonnull
    private static <T> BiFunction<String, String, AzureMessageHandler<T>> messageHandlerFactory(
            final AzureSubjectsProperties properties,
            final Converter<T> converter
    ) {
        final Function<String, Executor> executorFactory = executorFactor(properties);
        final Function<String, MessageHandlerOptions> optionsFactory = subscriptionOptionsFactory(properties);
        final BiFunction<String, String, SubscriptionClient> subscriptionFactory = subscriptionFactory(properties);

        return memoize((topicName, groupName) -> {
            final SubscriptionClient subscriptionClient = subscriptionFactory.apply(topicName, groupName);
            final Executor executor = executorFactory.apply(topicName);
            final MessageHandlerOptions options = optionsFactory.apply(topicName);

            final AzureMessageHandler<T> handler = new AzureMessageHandler<>(converter, executor);

            try {
                subscriptionClient.registerMessageHandler(handler, options);
            } catch (InterruptedException | ServiceBusException e) {
                throw new IllegalStateException("Can not subscribe", e);
            }

            return handler;
        });
    }

}
