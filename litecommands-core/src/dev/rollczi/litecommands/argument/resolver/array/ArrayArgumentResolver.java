package dev.rollczi.litecommands.argument.resolver.array;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.resolver.TypedArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayArgumentResolver<SENDER> extends TypedArgumentResolver<SENDER, Object, ArrayArgument> {

    private final ParserRegistry<SENDER> parserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;

    public ArrayArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(ArrayArgument.class);
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
    }

    @Override
    public ParseResult<Object> parseTyped(Invocation<SENDER> invocation, ArrayArgument context, RawInput rawInput) {
        return parse(this.getType(context), rawInput, context, invocation);
    }

    private <T> ParseResult<Object> parse(Class<T> componentType, RawInput rawInput, ArrayArgument arrayArgument, Invocation<SENDER> invocation) {
        Argument<T> argument = new SimpleArgument<>(arrayArgument.getKeyName(), WrapFormat.notWrapped(componentType));

        ParserSet<SENDER, T> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, RawInput, T> parser = parserSet.getParser(RawInput.class)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find parser for " + componentType.getName()));

        List<T> values = new ArrayList<>();


        while (rawInput.hasNext()) {
            int count = rawInput.seeAll().size();
            Range range = parser.getRange(argument);

            if (range.isBelowRange(count)) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
            }

            ParseResult<T> parsedResult = parser.parse(invocation, argument, rawInput);

            if (parsedResult.isFailed()) {
                return ParseResult.failure(parsedResult.getFailedReason());
            }

            values.add(parsedResult.getSuccess());
        }

        Object array = Array.newInstance(componentType, values.size());

        for (int i = 0; i < values.size(); i++) {
            Array.set(array, i, values.get(i));
        }

        return ParseResult.success(array);
    }

    @Override
    public Range getTypedRange(ArrayArgument argument) {
        return Range.moreThan(0);
    }

    @Override
    public SuggestionResult suggestTyped(Invocation<SENDER> invocation, ArrayArgument argument, SuggestionContext context) {
        return suggest(this.getType(argument), context, argument, invocation);
    }

    private <T> SuggestionResult suggest(Class<T> componentType, SuggestionContext context, ArrayArgument arrayArgument, Invocation<SENDER> invocation) {
        Argument<T> argument = new SimpleArgument<>(arrayArgument.getKeyName(), WrapFormat.notWrapped(componentType));

        ParserSet<SENDER, T> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, RawInput, T> parser = parserSet.getParser(RawInput.class)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find parser for " + componentType.getName()));

        Suggester<SENDER, T> suggester = suggesterRegistry.getSuggester(componentType, argument.getKey());

        SuggestionResult result = SuggestionResult.empty();

        Suggestion current = context.getCurrent();
        RawInput rawInput = RawInput.of(current.multilevelList());

        while (rawInput.hasNext()) {
            int count = rawInput.seeAll().size();
            Range range = parser.getRange(argument);

            if (range.isInRange(count) || range.isBelowRange(count)) {
                SuggestionContext suggestionContext = new SuggestionContext(Suggestion.from(rawInput.seeAll()));
                int beforeConsumed = suggestionContext.getConsumed();
                SuggestionResult suggestionResult = suggester.suggest(invocation, argument, suggestionContext);

                int afterConsumed = suggestionContext.getConsumed();

                if (afterConsumed >= beforeConsumed) {
                    Suggestion suggestion = current.deleteRight(afterConsumed);
                    return suggestionResult.appendLeft(suggestion.multilevelList());
                }

                rawInput = RawInput.of(suggestionContext.getCurrent().deleteLeft(afterConsumed).multilevelList());
                continue;
            }

            ParseResult<T> parsedResult = parser.parse(invocation, argument, rawInput);

            if (parsedResult.isFailed()) {
                return SuggestionResult.empty();
            }
        }

        return result;
    }

    private Class<Object> getType(ArrayArgument context) {
        Class<Object> arrayType = context.getWrapperFormat().getParsedType();

        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("ArrayArgumentResolver can only parse arrays");
        }

        Class<Object> componentType = (Class<Object>) arrayType.getComponentType();

        if (componentType == null) {
            throw new IllegalArgumentException("ArrayArgumentResolver cannot parse array of null");
        }

        return componentType;
    }

    @Override
    public boolean canParse(Invocation<SENDER> invocation, Argument<Object> argument, RawInput rawInput) {
        return argument.getWrapperFormat().getParsedType().isArray();
    }

}
