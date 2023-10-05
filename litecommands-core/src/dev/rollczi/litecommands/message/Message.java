package dev.rollczi.litecommands.message;

import java.util.function.Function;

/**
 * Message is used to store message in {@link MessageRegistry}
 * @see MessageRegistry
 * @param <T> type of message
 */
@FunctionalInterface
public interface Message<T, CONTEXT> {

    T get(CONTEXT context);

    /**
     * Create message from string
     */
    static <CONTEXT> Message<String, CONTEXT> of(String message) {
        return context -> message;
    }

    /**
     * Create message from string
     */
    static <CONTEXT> Message<String, CONTEXT> of(Function<CONTEXT, String> message) {
        return context -> message.apply(context);
    }

}
