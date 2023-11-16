package dev.rollczi.litecommands.argument.resolver.standard;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.time.DurationParser;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class DurationArgumentResolver<SENDER> extends TemporalAmountArgumentResolver<SENDER, Duration> {

    private static final List<String> SUGGESTIONS_LIST = Arrays.asList(
        "1s", "5s", "10s", "30s",
        "1m", "1m30s", "5m", "10m", "30m",
        "1h", "5h", "10h",
        "1d", "7d", "30d"
    );

    public DurationArgumentResolver() {
        super(DurationParser.DATE_TIME_UNITS, () -> SUGGESTIONS_LIST);
    }
}
