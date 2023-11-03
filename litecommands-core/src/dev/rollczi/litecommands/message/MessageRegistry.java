package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageRegistry<SENDER> {

    private final Map<MessageKey<?>, Message<?, ?>> messages = new HashMap<>();
    private final Map<MessageKey<?>, InvokedMessage<SENDER, ?, ?>> invokedMessages = new HashMap<>();

    public <T, CONTEXT> void register(MessageKey<CONTEXT> key, Message<T, CONTEXT> message) {
        messages.put(key, message);
    }

    public <T, CONTEXT> void register(MessageKey<CONTEXT> key, InvokedMessage<SENDER, T, CONTEXT> message) {
        invokedMessages.put(key, message);
    }

    public Optional<Object> getInvoked(MessageKey<Void> key, Invocation<SENDER> invocation) {
        return getInvoked(key, invocation, null);
    }

    @SuppressWarnings("unchecked")
    public <CONTEXT> Optional<Object> getInvoked(MessageKey<CONTEXT> key, Invocation<SENDER> invocation, CONTEXT context) {
        InvokedMessage<SENDER, ?, CONTEXT> invokedMessage = (InvokedMessage<SENDER, ?, CONTEXT>) invokedMessages.get(key);

        if (invokedMessage != null) {
            return Optional.of(invokedMessage.get(invocation, context));
        }

        return get(key, context);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <CONTEXT> Optional<Object> get(MessageKey<CONTEXT> key, CONTEXT context) {
        Message<?, CONTEXT> message = (Message<?, CONTEXT>) messages.get(key);

        if (message == null) {
            return Optional.of(key.getDefaultMessage(context));
        }

        return Optional.of(message.get(context));
    }

    @Deprecated
    public Optional<Object> get(MessageKey<Void> key) {
        return get(key, null);
    }

}
