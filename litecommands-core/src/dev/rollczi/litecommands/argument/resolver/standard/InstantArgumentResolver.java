package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

public class InstantArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Instant> {

    private static final SuggestionResult SUGGESTIONS = Stream.of(
        "2021-12-25T08:00:00Z",
        "2023-09-20T14:00:00+02:00",
        "2023-07-29T09:40:30+03:00"
    ).collect(SuggestionResult.collector());

    @Override
    protected ParseResult<Instant> parse(Invocation<SENDER> invocation, Argument<Instant> context, String argument) {
        try {
            return ParseResult.success(Instant.parse(argument));
        } catch (DateTimeParseException e) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Instant> argument, SuggestionContext context) {
        return SUGGESTIONS;
    }
}
