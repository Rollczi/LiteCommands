package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.time.PeriodParser;

import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class PeriodArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Period> {

    private static final List<String> SUGGESTIONS_LIST = Arrays.asList(
        "1h", "3h", "7h",
        "1d", "7d", "30d",
        "1m", "3m", "6m", "1y", "5y"
    );

    @Override
    protected ParseResult<Period> parse(Invocation<SENDER> invocation, Argument<Period> context, String argument) {
        try {
            Period parse = PeriodParser.DATE_UNITS.parse(argument);

            return ParseResult.success(parse);
        } catch (IllegalArgumentException e) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Period> argument, SuggestionContext context) {
        return SuggestionResult.of(SUGGESTIONS_LIST);
    }
}
