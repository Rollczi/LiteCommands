package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.shared.Preconditions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;

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
    private final List<MessageKey<CONTEXT>> fallbacks;

    public MessageKey(String name, Function<CONTEXT, Object> defaultMessage) {
        this(name, defaultMessage, Collections.emptyList());
    }

    private MessageKey(String name, Function<CONTEXT, Object> defaultMessage, List<MessageKey<CONTEXT>> fallbacks) {
        this.name = name;
        this.defaultMessage = defaultMessage;
        this.fallbacks = fallbacks;
    }

    public String getName() {
        return name;
    }

    public List<MessageKey<CONTEXT>> getFallbacks() {
        return fallbacks;
    }

    public Object getDefaultMessage(CONTEXT context) {
        return defaultMessage.apply(context);
    }

    @SafeVarargs
    @ApiStatus.Experimental
    public final MessageKey<CONTEXT> withFallbacks(MessageKey<CONTEXT>... fallbacks) {
        return withFallbacks(Arrays.asList(fallbacks));
    }

    @ApiStatus.Experimental
    public MessageKey<CONTEXT> withFallbacks(List<MessageKey<CONTEXT>> fallbacks) {
        Preconditions.notNull(fallbacks, "fallbacks");
        Preconditions.notEmpty(fallbacks, "fallbacks");
        return new MessageKey<>(this.name, this.defaultMessage, Collections.unmodifiableList(fallbacks));
    }

    @SafeVarargs
    public static <CONTEXT> MessageKey<CONTEXT> of(String name, Object defaultMessage, MessageKey<CONTEXT>... fallbacks) {
        return MessageKey.of(name, context -> defaultMessage, fallbacks);
    }

    @SafeVarargs
    public static <CONTEXT> MessageKey<CONTEXT> of(String name, Function<CONTEXT, Object> defaultMessage, MessageKey<CONTEXT>... fallbacks) {
        Preconditions.notNull(name, "name");
        Preconditions.notNull(defaultMessage, "defaultMessage");
        List<MessageKey<CONTEXT>> fallbacksList = fallbacks == null || fallbacks.length == 0
            ? Collections.emptyList()
            : Collections.unmodifiableList(Arrays.asList(fallbacks)
        );
        return new MessageKey<>(name, defaultMessage, fallbacksList);
    }

}
