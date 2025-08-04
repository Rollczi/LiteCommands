package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;

class AdventureKeyArgument<SENDER> extends ArgumentResolver<SENDER, Key> {

    private final MessageRegistry<SENDER> messageRegistry;

    AdventureKeyArgument(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public ParseResult<Key> parse(Invocation<SENDER> invocation, Argument<Key> argument, String unparsedKey) {
        try {
            return ParseResult.success(Key.key(unparsedKey));
        }
        catch (InvalidKeyException invalidKeyException) {
            return ParseResult.failure(messageRegistry.get(LiteAdventureMessages.ADVENTURE_KEY_INVALID, invocation, unparsedKey));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Key> argument, SuggestionContext context) {
        return SuggestionResult.of("namespace:key");
    }

}
