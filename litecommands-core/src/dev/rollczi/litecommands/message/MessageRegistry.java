package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

public class MessageRegistry<SENDER> {

    private final Map<MessageKey<?>, Message<?, ?>> messages = new HashMap<>();
    private final Map<MessageKey<?>, InvokedMessage<SENDER, ?, ?>> invokedMessages = new HashMap<>();

    public <T, CONTEXT> void register(@MagicConstant(valuesFromClass = LiteMessages.class) MessageKey<CONTEXT> key, Message<T, CONTEXT> message) {
        messages.put(key, message);
    }

    public <T, CONTEXT> void register(@MagicConstant(valuesFromClass = LiteMessages.class) MessageKey<CONTEXT> key, InvokedMessage<SENDER, T, CONTEXT> message) {
        invokedMessages.put(key, message);
    }

    public Optional<Object> get(@MagicConstant(valuesFromClass = LiteMessages.class) MessageKey<Void> key, Invocation<SENDER> invocation) {
        return get(key, invocation, null);
    }

    @SuppressWarnings("unchecked")
    public <CONTEXT> Optional<Object> get(@MagicConstant(valuesFromClass = LiteMessages.class) MessageKey<CONTEXT> key, Invocation<SENDER> invocation, CONTEXT context) {
        Object invokedMessage = getRegisteredMessage(key, invocation, context);
        if (invokedMessage != null) {
            return Optional.of(invokedMessage);
        }

        for (MessageKey<CONTEXT> fallback : key.getFallbacks()) {
            Object fallbackMessage = getRegisteredMessage(fallback, invocation, context);
            if (fallbackMessage != null) {
                return Optional.of(fallbackMessage);
            }
        }

        return Optional.of(key.getDefaultMessage(context));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <CONTEXT> Object getRegisteredMessage(@MagicConstant(valuesFromClass = LiteMessages.class) MessageKey<CONTEXT> key, Invocation<SENDER> invocation, CONTEXT context) {
        InvokedMessage<SENDER, ?, CONTEXT> invokedMessage = (InvokedMessage<SENDER, ?, CONTEXT>) invokedMessages.get(key);
        if (invokedMessage != null) {
            return invokedMessage.get(invocation, context);
        }

        Message<?, CONTEXT> message = (Message<?, CONTEXT>) messages.get(key);
        if (message != null) {
            return message.get(context);
        }

        return null;
    }

}
