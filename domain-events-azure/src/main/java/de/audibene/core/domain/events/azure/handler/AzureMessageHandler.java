package de.audibene.core.domain.events.azure.handler;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import de.audibene.core.domain.events.subject.converter.Converter;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AzureMessageHandler<T> implements IMessageHandler {

    private final Collection<Consumer<T>> listeners = new LinkedHashSet<>();

    private final Converter<T> converter;
    private final Executor executor;

    public AzureMessageHandler(final Converter<T> converter, final Executor executor) {
        this.converter = converter;
        this.executor = executor;
    }

    public void subscribe(final Consumer<T> consumer){
        this.listeners.add(consumer);
    }

    @Nonnull
    @Override
    public CompletableFuture<Void> onMessageAsync(final IMessage message) {
        return CompletableFuture
                .completedFuture(new String(message.getBody()))
                .thenApplyAsync(converter::parse, executor)
                .thenComposeAsync(event -> CompletableFuture.allOf(notifyListeners(event)), executor);
    }

    @Nonnull
    private CompletableFuture[] notifyListeners(final T event) {
        return listeners
                .stream()
                .map(listener -> CompletableFuture.runAsync(() -> listener.accept(event), executor))
                .toArray(CompletableFuture[]::new);
    }

    @Override
    public void notifyException(final Throwable exception, final ExceptionPhase phase) {
        //ignore
    }
}
