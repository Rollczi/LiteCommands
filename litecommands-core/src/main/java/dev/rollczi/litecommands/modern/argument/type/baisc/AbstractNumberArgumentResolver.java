package dev.rollczi.litecommands.modern.argument.type.baisc;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.argument.type.OneArgumentResolver;
import dev.rollczi.litecommands.modern.command.CommandExecuteError;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.Suggestion;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractNumberArgumentResolver<SENDER, T extends Number> extends OneArgumentResolver<SENDER, T> {

    private final List<Suggestion> suggestions;
    private final Function<String, T> parser;

    public AbstractNumberArgumentResolver(List<T> suggestions, Function<String, T> parser) {
        this.suggestions = suggestions.stream()
            .map(Object::toString)
            .map(Suggestion::of)
            .collect(Collectors.toList());
        this.parser = parser;
    }

    @Override
    protected ArgumentResult<T> parse(Invocation<SENDER> invocation, Argument<T> context, String argument) {
        try {
            return ArgumentResult.success(() -> parser.apply(argument));
        }
        catch (NumberFormatException e) {
            return ArgumentResult.failure(this.failedReason(invocation, context, argument));
        }
    }

    protected FailedReason failedReason(Invocation<SENDER> invocation, Argument<T> context, String argument) {
        return FailedReason.of(CommandExecuteError.INVALID_ARGUMENT);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> context, SuggestionContext suggestion) {
        String input = suggestion.getCurrent();

        T apply = parser.apply(input);

        double upper = apply.doubleValue() * 10;

        SuggestionResult result = SuggestionResult.from(this.suggestions);

        IntStream.of(0, 1, 5)
            .mapToObj(i -> i * upper)
            .map(Object::toString)
            .forEach(numberText -> result.with(Suggestion.of(numberText)));

        return result;
    }

    public static <SENDER, T extends Number> OneArgumentResolver<SENDER, T> of(List<T> suggestions, Function<String, T> parser) {
        return new AbstractNumberArgumentResolver<SENDER, T>(suggestions, parser) {};
    }

    public static <SENDER> OneArgumentResolver<SENDER, Integer> ofInteger() {
        return AbstractNumberArgumentResolver.of(Arrays.asList(0, 1, 5, 10, 50, 100, 500), Integer::parseInt);
    }

    public static <SENDER> OneArgumentResolver<SENDER, Double> ofDouble() {
        return AbstractNumberArgumentResolver.of(Arrays.asList(0.0, 0.5, 1.0, 1.5, 5.5), Double::parseDouble);
    }
}
