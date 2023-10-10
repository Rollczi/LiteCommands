package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.time.DurationParser;

import java.time.Duration;

public class DurationArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Duration> {

    @Override
    protected ParseResult<Duration> parse(Invocation<SENDER> invocation, Argument<Duration> context, String argument) {
        try {
            Duration parse = DurationParser.DATE_TIME_UNITS.parse(argument);

            return ParseResult.success(parse);
        }
        catch (IllegalArgumentException e) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Duration> argument, SuggestionContext context) {
        return SuggestionResult.of("1d"); // You can customize suggestions if needed
    }

    // /command 1d
}
