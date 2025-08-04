package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import net.kyori.adventure.key.Key;

class AdventureKeyArgument<SENDER> implements Parser<SENDER, Key> {
    private final MessageRegistry<SENDER> messageRegistry;

    AdventureKeyArgument(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("PatternValidation") // validation is done by methods in Key class
    @Override
    public ParseResult<Key> parse(Invocation<SENDER> invocation, Argument<Key> argument, RawInput input) {
        final String string = input.next();

        int index = string.indexOf(':');
        Key parsed;
        if (index == -1) {
            parsed = null;
        } else {
            String namespace = string.substring(0, index);
            String value = string.substring(index + 1);
            if (namespace.isEmpty() || value.isEmpty() || !(Key.parseableNamespace(namespace) && Key.parseableValue(value))) {
                parsed = null;
            } else {
                parsed = Key.key(namespace, value);
            }
        }
        if (parsed != null) {
            return ParseResult.success(parsed);
        } else {
            return ParseResult.failure(messageRegistry.get(LiteAdventureMessages.NAMESPACED_KEY_INVALID, invocation, input));
        }
    }

    @Override
    public Range getRange(Argument<Key> keyArgument) {
        return Range.ONE;
    }
}
