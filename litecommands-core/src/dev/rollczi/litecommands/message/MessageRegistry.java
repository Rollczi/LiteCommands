package dev.rollczi.litecommands.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageRegistry {

    private final Map<MessageKey<?>, Message<?, ?>> messages = new HashMap<>();

    public <T, CONTEXT> void register(MessageKey<CONTEXT> key, Message<T, CONTEXT> message) {
        messages.put(key, message);
    }

    @SuppressWarnings("unchecked")
    public <CONTEXT> Optional<Object> get(MessageKey<CONTEXT> key, CONTEXT context) {
        Message<?, CONTEXT> message = (Message<?, CONTEXT>) messages.get(key);

        if (message == null) {
            return Optional.of(key.getDefaultMessage());
        }

        return Optional.of(message.get(context));
    }

    public Optional<Object> get(MessageKey<Void> key) {
        return get(key, null);
    }

}
