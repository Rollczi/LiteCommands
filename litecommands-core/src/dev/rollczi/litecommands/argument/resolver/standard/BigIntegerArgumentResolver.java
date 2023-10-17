package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.math.BigInteger;
import java.util.stream.Stream;

public class BigIntegerArgumentResolver<SENDER> extends ArgumentResolver<SENDER, BigInteger> {

    private static final SuggestionResult SUGGESTIONS = Stream.of(
        "1843342345",
        "-4374629832",
        "843787420427384239740947328943742734372984",
        "0"
    ).collect(SuggestionResult.collector());

    private final MessageRegistry<SENDER> messageRegistry;

    public BigIntegerArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<BigInteger> parse(Invocation<SENDER> invocation, Argument<BigInteger> context, String argument) {
        try {
            return ParseResult.success(new BigInteger(argument));
        } catch (NumberFormatException ignored) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteMessages.INVALID_NUMBER, invocation, argument));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<BigInteger> argument, SuggestionContext context) {
        return SUGGESTIONS;
    }
}
