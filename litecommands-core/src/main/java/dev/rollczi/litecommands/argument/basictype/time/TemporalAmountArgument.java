package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.InvalidUsage;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.shared.EstimatedTemporalAmountParser;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TemporalAmountArgument<T extends TemporalAmount> implements MultilevelArgument<T> {

    private static final String MULTI_LEVEL_ARGUMENT_SEPARATOR = " ";

    private final EstimatedTemporalAmountParser<T> parser;
    private final Supplier<List<T>> suggestedTemporal;

    protected TemporalAmountArgument(EstimatedTemporalAmountParser<T> parser, Supplier<List<T>> suggestedTemporal) {
        this.parser = parser;
        this.suggestedTemporal = suggestedTemporal;
    }

    @Override
    public Result<T, ?> parseMultilevel(LiteInvocation invocation, String... arguments) {
        return this.parseTemporal(String.join(MULTI_LEVEL_ARGUMENT_SEPARATOR, arguments))
            .mapErr(error -> InvalidUsage.INSTANCE);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.suggestedTemporal.get().stream()
            .map(t -> Suggestion.of(parser.format(t)))
            .collect(Collectors.toList());
    }

    @Override
    public int countMultilevel() {
        return 1;
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.parseTemporal(suggestion.multilevel()).isOk();
    }

    private Result<T, IllegalArgumentException> parseTemporal(String arguments) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> parser.parse(arguments));
    }

}
