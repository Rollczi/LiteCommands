package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TemporalAccessorArgument<T extends TemporalAccessor> implements MultilevelArgument<T> {

    private static final String MULTI_LEVEL_ARGUMENT_SEPARATOR = " ";

    private final DateTimeFormatter formatter;
    private final TemporalQuery<T> query;
    private final Supplier<List<T>> suggestedTemporal;

    private final int argumentCount;

    protected TemporalAccessorArgument(DateTimeFormatter formatter, TemporalQuery<T> query, Supplier<List<T>> suggestedTemporal) {
        this.formatter = formatter;
        this.argumentCount = formatter.toString().split(" ").length;
        this.query = query;
        this.suggestedTemporal = suggestedTemporal;
    }

    protected TemporalAccessorArgument(String formatterPattern, TemporalQuery<T> query, Supplier<List<T>> suggestedTemporal) {
        this(
            DateTimeFormatter.ofPattern(formatterPattern, Locale.ROOT),
            query,
            suggestedTemporal
        );
    }

    @Override
    public Result<T, ?> parseMultilevel(LiteInvocation invocation, String... arguments) {
        return this.parseTemporal(String.join(MULTI_LEVEL_ARGUMENT_SEPARATOR, arguments))
            .mapErr(dateTimeParseException -> "Invalid temporal format");
    }

    @Override
    public int countMultilevel() {
        return argumentCount;
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.suggestedTemporal.get().stream()
            .map(formatter::format)
            .map(suggestion -> suggestion.split(MULTI_LEVEL_ARGUMENT_SEPARATOR))
            .map(Suggestion::multilevel)
            .collect(Collectors.toList());
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.parseTemporal(suggestion.multilevel()).isOk();
    }

    private Result<T, DateTimeParseException> parseTemporal(String arguments) {
        return Result.supplyThrowing(DateTimeParseException.class, () -> formatter.parse(arguments, query));
    }

}
