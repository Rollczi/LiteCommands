package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.shared.Preconditions;

import java.util.function.Function;

/**
 * Message key is used to get message from {@link MessageRegistry}
 * You can register your own message {@link MessageRegistry#register(MessageKey, Message)}
 * If you want to use default message key, you can use {@link LiteMessages}
 *
 * @see MessageRegistry
 * @see LiteMessages
 */
public class MessageKey<CONTEXT> {

    private final String name;
    private final Function<CONTEXT, Object> defaultMessage;

    private MessageKey(String name, Function<CONTEXT, Object> defaultMessage) {
        this.name = name;
        this.defaultMessage = defaultMessage;
    }
    public String getName() {
        return name;
    }

    public Object getDefaultMessage(CONTEXT context) {
        return defaultMessage.apply(context);
    }

    public static <CONTEXT> MessageKey<CONTEXT> of(String name, Object defaultMessage) {
        return of(name, context -> defaultMessage);
    }

    public static <CONTEXT> MessageKey<CONTEXT> of(String name, Function<CONTEXT, Object> defaultMessage) {
        Preconditions.notNull(name, "name");
        Preconditions.notNull(defaultMessage, "defaultMessage");
        return new MessageKey<>(name, defaultMessage);
    }

}
