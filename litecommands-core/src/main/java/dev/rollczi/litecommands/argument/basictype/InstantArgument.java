package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InstantArgument implements MultilevelArgument<Instant> {

    private static final String MULTI_LEVEL_ARGUMENT_SEPARATOR = " ";
    private static final int INSTANT_SUGGESTION_MINIMUM_BOUND = 0;
    private static final int INSTANT_SUGGESTION_MAXIMUM_BOUND = 7;
    private final DateFormat dateFormat;

    public InstantArgument(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Result<Instant, ?> parseMultilevel(LiteInvocation invocation, String... arguments) {
        return this.parseInstant(String.join(MULTI_LEVEL_ARGUMENT_SEPARATOR, arguments))
            .toResult(Blank.BLANK);

    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        Instant now = Instant.now();
        return IntStream.rangeClosed(INSTANT_SUGGESTION_MINIMUM_BOUND, INSTANT_SUGGESTION_MAXIMUM_BOUND)
            .mapToObj(daysCount -> now.plus(daysCount, ChronoUnit.DAYS))
            .map(Date::from)
            .map(dateFormat::format)
            .map(suggestion -> suggestion.split(MULTI_LEVEL_ARGUMENT_SEPARATOR))
            .map(Suggestion::multilevel)
            .collect(Collectors.toList());
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.parseInstant(suggestion.multilevel()).isPresent();
    }

    @Override
    public int countMultilevel() {
        return 2;
    }

    private Option<Instant> parseInstant(String arg) {
        return Option.supplyThrowing(ParseException.class, () -> dateFormat.parse(arg))
            .map(Date::toInstant);
    }

}
