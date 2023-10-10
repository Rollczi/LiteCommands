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
import java.util.Arrays;
import java.util.List;

public class DurationArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Duration> {

    static final List<String> SUGGESTIONS_LIST = Arrays.asList("7d", "7h", "7m", "7s", "7ms", "7ns");


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
        return SuggestionResult.of(SUGGESTIONS_LIST);
    }
}
