package dev.rollczi.litecommands.modern.core.argument;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.argument.api.OneArgument;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractNumberArgument<SENDER, T extends Number> extends OneArgument<SENDER, T> {

    private final List<Suggestion> suggestions;
    private final Function<String, T> parser;

    public AbstractNumberArgument(List<T> suggestions, Function<String, T> parser) {
        this.suggestions = suggestions.stream()
            .map(Object::toString)
            .map(Suggestion::of)
            .collect(Collectors.toList());
        this.parser = parser;
    }

    @Override
    protected ArgumentResult<T> parse(Invocation<SENDER> invocation, Argument<Object, T> context, String argument) {
        try {
            return ArgumentResult.success(() -> parser.apply(argument));
        }
        catch (NumberFormatException e) {
            return ArgumentResult.failure(this.failedReason(invocation, context, argument));
        }
    }

    protected FailedReason failedReason(Invocation<SENDER> invocation, Argument<Object, T> context, String argument) {
        return FailedReason.empty();
    }

    @Override
    public List<Suggestion> suggestion(Invocation<SENDER> invocation, Argument<Object, T> context, Suggestion suggestion) {
        Suggestion last = suggestion.slashLevel(suggestion.lengthMultilevel() - 1);
        String input = last.single();

        T apply = parser.apply(input);

        double upper = apply.doubleValue() * 10;

        List<Suggestion> suggestions = new ArrayList<>(this.suggestions);

        IntStream.of(0, 1, 5)
            .mapToObj(i -> i * upper)
            .map(Object::toString)
            .map(Suggestion::of)
            .forEach(suggestions::add);

        return suggestions;
    }

    public static <SENDER, T extends Number> OneArgument<SENDER, T> of(List<T> suggestions, Function<String, T> parser) {
        return new AbstractNumberArgument<SENDER, T>(suggestions, parser) {};
    }

    public static <SENDER> OneArgument<SENDER, Integer> ofInteger() {
        return AbstractNumberArgument.of(Arrays.asList(0, 1, 5, 10, 50, 100, 500), Integer::parseInt);
    }

    public static <SENDER> OneArgument<SENDER, Double> ofDouble() {
        return AbstractNumberArgument.of(Arrays.asList(0.0, 0.5, 1.0, 1.5, 5.5), Double::parseDouble);
    }
}
