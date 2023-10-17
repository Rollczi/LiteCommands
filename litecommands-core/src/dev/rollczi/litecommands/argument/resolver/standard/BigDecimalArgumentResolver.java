package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

public class BigDecimalArgumentResolver<SENDER> extends ArgumentResolver<SENDER, BigDecimal> {

    private static final SuggestionResult SUGGESTIONS = Stream.of(
        "18.48",
        "-90.374329",
        "0.3728933"
    ).collect(SuggestionResult.collector());

    private final MessageRegistry<SENDER> messageRegistry;

    public BigDecimalArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<BigDecimal> parse(Invocation<SENDER> invocation, Argument<BigDecimal> context, String argument) {
        try {
            return ParseResult.success(new BigDecimal(argument));
        } catch (NumberFormatException ignored) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteMessages.INVALID_NUMBER, invocation, argument));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<BigDecimal> argument, SuggestionContext context) {
        return SUGGESTIONS;
    }
}
