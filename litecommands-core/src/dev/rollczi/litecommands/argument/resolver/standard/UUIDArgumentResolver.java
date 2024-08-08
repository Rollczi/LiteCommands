package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UUIDArgumentResolver<SENDER> extends ArgumentResolver<SENDER, UUID> {

    private final MessageRegistry<SENDER> messageRegistry;
    private final SuggestionResult suggestions;

    public UUIDArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;

        // We want to show 10 random UUIDs in both with-dashes and without-dashes formats
        suggestions = Stream
            .generate(() -> UUID.randomUUID().toString())
            .limit(10)
            .map(str -> ThreadLocalRandom.current().nextBoolean() ? str.replace("-", "") : str)
            .collect(SuggestionResult.collector());
    }

    @Override
    protected ParseResult<UUID> parse(Invocation<SENDER> invocation, Argument<UUID> context, String argument) {
        try {
            // Format with dashes
            if (argument.length() == 36) {
                return ParseResult.success(UUID.fromString(argument));
            }

            // Format without dashes
            if (argument.length() == 32) {
                return ParseResult.success(UUID.fromString(
                    new StringBuilder(argument)
                        .insert(8, '-')
                        .insert(13, '-')
                        .insert(18, '-')
                        .insert(23, '-')
                        .toString()
                ));
            }
        } catch (IllegalArgumentException ignored) {
        }

        return ParseResult.failure(messageRegistry.getInvoked(LiteMessages.UUID_INVALID_FORMAT, invocation, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<UUID> argument, SuggestionContext context) {
        return suggestions;
    }

}
