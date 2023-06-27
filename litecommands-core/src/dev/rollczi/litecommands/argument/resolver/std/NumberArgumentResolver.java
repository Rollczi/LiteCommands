package dev.rollczi.litecommands.argument.resolver.std;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberArgumentResolver<SENDER, T extends Number> extends ArgumentResolver<SENDER, T> {

    private final Function<String, T> parser;
    private final SuggestionResult suggestions;
    private final boolean generateSuggestions;

    protected NumberArgumentResolver(Function<String, T> parser, List<T> suggestions, boolean generateSuggestions) {
        this.parser = parser;
        this.suggestions = suggestions.stream()
            .map(Object::toString)
            .collect(SuggestionResult.collector());

        this.generateSuggestions = generateSuggestions;
    }

    @Override
    protected ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> context, String argument) {
        try {
            T applied = parser.apply(argument);

            return ParseResult.success(applied);
        }
        catch (NumberFormatException e) {
            return ParseResult.failure(this.failedReason(invocation, context, argument));
        }
    }

    protected FailedReason failedReason(Invocation<SENDER> invocation, Argument<T> context, String argument) {
        return FailedReason.of(InvalidUsage.Cause.INVALID_ARGUMENT);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context) {
        if (!this.generateSuggestions) {
            return this.suggestions;
        }

        try {
            String input = context.getCurrent().lastLevel();

            T apply = parser.apply(input);
            double upper = apply.doubleValue() * 10;

            SuggestionResult result = SuggestionResult.from(this.suggestions.getSuggestions());

            IntStream.of(1, 2, 5)
                .mapToObj(i -> i * upper)
                .map(Object::toString)
                .forEach(numberText -> result.add(Suggestion.of(numberText)));

            return result;
        }
        catch (NumberFormatException ignored) {
            return this.suggestions;
        }
    }

    public static <SENDER, T extends Number> ArgumentResolver<SENDER, T> of(Function<String, T> parser, List<T> suggestions) {
        return new NumberArgumentResolver<>(parser, suggestions, true);
    }

    public static <SENDER, T extends Number> ArgumentResolver<SENDER, T> of(Function<String, T> parser, List<T> suggestions, boolean generateSuggestions) {
        return new NumberArgumentResolver<>(parser, suggestions, generateSuggestions);
    }

    public static <SENDER> ArgumentResolver<SENDER, Integer> ofInteger() {
        return NumberArgumentResolver.of(Integer::parseInt, Arrays.asList(0, 1, 5, 10, 50, 100, 500));
    }

    public static <SENDER> ArgumentResolver<SENDER, Double> ofDouble() {
        return NumberArgumentResolver.of(Double::parseDouble, Arrays.asList(0.0, 0.5, 1.0, 1.5, 5.5));
    }

    public static <SENDER> ArgumentResolver<SENDER, Float> ofFloat() {
        return NumberArgumentResolver.of(Float::parseFloat, Arrays.asList(0.0f, 0.5f, 1.0f, 1.5f, 5.5f));
    }

    public static <SENDER> ArgumentResolver<SENDER, Long> ofLong() {
        return NumberArgumentResolver.of(Long::parseLong, Arrays.asList(0L, 1L, 5L, 10L, 50L, 100L, 500L));
    }

    public static <SENDER> ArgumentResolver<SENDER, Short> ofShort() {
        return NumberArgumentResolver.of(Short::parseShort, Arrays.asList((short) 0, (short) 1, (short) 5, (short) 10, (short) 50, (short) 100, (short) 500));
    }

    public static <SENDER> ArgumentResolver<SENDER, Byte> ofByte() {
        return NumberArgumentResolver.of(
            Byte::parseByte,
            IntStream.range(-128, 127)
                .mapToObj(i -> (byte) i)
                .collect(Collectors.toList()),
            false
        );
    }

}
