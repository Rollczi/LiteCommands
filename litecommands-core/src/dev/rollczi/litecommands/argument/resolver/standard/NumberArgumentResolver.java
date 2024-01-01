package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import dev.rollczi.litecommands.util.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberArgumentResolver<SENDER, T extends Number> extends ArgumentResolver<SENDER, T> {

    private static final String DECIMAL_SEPARATOR = ".";
    private static final String MINUS_SYMBOL = "-";

    private static final String[] SMALL_NUMBERS_SUGGESTIONS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final String[] BIG_NUMBERS_SUGGESTIONS = {"0", "1", "5"};
    private static final Pattern VALID_NUMBER_PATTERN = Pattern.compile("^-?\\d*(\\.?\\d*)?$");

    private final Function<String, T> parser;
    private final SuggestionResult suggestions;
    private final boolean generateSuggestions;
    private final boolean withDecimal;

    protected NumberArgumentResolver(Function<String, T> parser, List<T> suggestions, boolean generateSuggestions, boolean withDecimal) {
        this.parser = parser;
        this.suggestions = suggestions.stream()
            .map(Object::toString)
            .collect(SuggestionResult.collector());

        this.generateSuggestions = generateSuggestions;
        this.withDecimal = withDecimal;
    }

    @Override
    protected ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> context, String argument) {
        try {
            T applied = parser.apply(argument);

            return ParseResult.success(applied);
        } catch (NumberFormatException e) {
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

        String input = context.getCurrent().lastLevel();

        if (!isPotentialNumber(input)) {
            return this.suggestions;
        }

        SuggestionResult generated = SuggestionResult.from(this.suggestions.getSuggestions());

        if (input.isEmpty()) {
            generated.add(Suggestion.of(MINUS_SYMBOL));
        }

        if (withDecimal && !input.contains(DECIMAL_SEPARATOR)) {
            generated.add(Suggestion.of(input + DECIMAL_SEPARATOR));
        }

        String[] numberSuggestions = isBigNumber(input) ? BIG_NUMBERS_SUGGESTIONS : SMALL_NUMBERS_SUGGESTIONS;

        for (String numberSuggestion : numberSuggestions) {
            generated.add(Suggestion.of(input + numberSuggestion));
        }

        return generated;
    }

    private static boolean isBigNumber(String input) {
        int realLength = input
            .replace(DECIMAL_SEPARATOR, StringUtil.EMPTY)
            .replace(MINUS_SYMBOL, StringUtil.EMPTY)
            .length();

        return realLength > 1;
    }

    private boolean isPotentialNumber(String argument) {
        return VALID_NUMBER_PATTERN.matcher(argument).matches();
    }

    public static <SENDER, T extends Number> ArgumentResolver<SENDER, T> of(Function<String, T> parser, List<T> suggestions) {
        return new NumberArgumentResolver<>(parser, suggestions, true, false);
    }

    public static <SENDER, T extends Number> ArgumentResolver<SENDER, T> of(Function<String, T> parser, List<T> suggestions, boolean generateSuggestions) {
        return new NumberArgumentResolver<>(parser, suggestions, generateSuggestions, false);
    }

    public static <SENDER, T extends Number> ArgumentResolver<SENDER, T> of(Function<String, T> parser, List<T> suggestions, boolean generateSuggestions, boolean withDecimal) {
        return new NumberArgumentResolver<>(parser, suggestions, generateSuggestions, withDecimal);
    }

    public static <SENDER> ArgumentResolver<SENDER, Integer> ofInteger() {
        return NumberArgumentResolver.of(Integer::parseInt, Arrays.asList(10, 50, 100, 500));
    }

    public static <SENDER> ArgumentResolver<SENDER, Double> ofDouble() {
        return NumberArgumentResolver.of(Double::parseDouble, Collections.emptyList(), true, true);
    }

    public static <SENDER> ArgumentResolver<SENDER, Float> ofFloat() {
        return NumberArgumentResolver.of(Float::parseFloat, Collections.emptyList(), true, true);
    }

    public static <SENDER> ArgumentResolver<SENDER, Long> ofLong() {
        return NumberArgumentResolver.of(Long::parseLong, Arrays.asList(10L, 50L, 100L, 500L));
    }

    public static <SENDER> ArgumentResolver<SENDER, Short> ofShort() {
        return NumberArgumentResolver.of(Short::parseShort, Arrays.asList((short) 10, (short) 50, (short) 100, (short) 500));
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
