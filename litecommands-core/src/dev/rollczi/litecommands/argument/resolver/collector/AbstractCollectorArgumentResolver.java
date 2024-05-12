package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.resolver.TypedArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public abstract class AbstractCollectorArgumentResolver<SENDER, E, COLLECTION> extends TypedArgumentResolver<SENDER, COLLECTION, CollectorArgument<COLLECTION>> {

    private final ParserRegistry<SENDER> parserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;

    protected AbstractCollectorArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(CollectorArgument.class);
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
    }

    @Override
    public ParseResult<COLLECTION> parseTyped(Invocation<SENDER> invocation, CollectorArgument<COLLECTION> context, RawInput rawInput) {
        return parse(this.getElementType(context, invocation), rawInput, context, invocation);
    }

    private ParseResult<COLLECTION> parse(Class<E> componentType, RawInput rawInput, CollectorArgument<COLLECTION> collectorArgument, Invocation<SENDER> invocation) {
        ParseResult<List<E>> parseResult = parseToList(componentType, rawInput, collectorArgument, invocation);

        if (parseResult.isFailed()) {
            return ParseResult.failure(parseResult.getFailedReason());
        }

        Collector<E, ?, ? extends COLLECTION> collector = getCollector(collectorArgument, invocation);
        COLLECTION result = parseResult.getSuccess().stream()
            .collect(collector);

        return ParseResult.success(result);
    }

    private ParseResult<List<E>> parseToList(Class<E> componentType, RawInput rawInput, CollectorArgument<COLLECTION> collectorArgument, Invocation<SENDER> invocation) {
        Argument<E> argument = new SimpleArgument<>(collectorArgument.getKeyName(), WrapFormat.notWrapped(componentType));

        ParserSet<SENDER, E> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, E> parser = parserSet.getValidParserOrThrow(invocation, argument);
        Range range = parser.getRange(argument);
        String delimiter = collectorArgument.getDelimiter();

        if (delimiter.equals(RawCommand.COMMAND_SEPARATOR)) {
            return parseWithSpaceDelimiter(rawInput, invocation, range, parser, argument);
        }

        return parseWithNoSpaceDelimiter(rawInput, range, delimiter, invocation, argument, parser);
    }

    private static <SENDER, E> ParseResult<List<E>> parseWithSpaceDelimiter(RawInput rawInput, Invocation<SENDER> invocation, Range range, Parser<SENDER, E> parser, Argument<E> argument) {
        List<E> results = new ArrayList<>();

        while (rawInput.hasNext()) {
            int count = rawInput.seeAll().size();

            if (range.isBelowRange(count)) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
            }

            ParseResult<E> parsedResult = parser.parse(invocation, argument, rawInput);

            if (parsedResult.isFailed()) {
                return ParseResult.failure(parsedResult.getFailedReason());
            }

            results.add(parsedResult.getSuccess());
        }

        return ParseResult.success(results);
    }

    private ParseResult<List<E>> parseWithNoSpaceDelimiter(RawInput rawInput, Range range, String delimiter, Invocation<SENDER> invocation, Argument<E> argument, Parser<SENDER, E> parser) {
        List<String> input = new ArrayList<>(rawInput.seeAll());

        if (input.isEmpty()) {
            return ParseResult.success(Collections.emptyList());
        }

        StringBuilder buffer = new StringBuilder();
        int bufferMultilevel = 0; // used to count the current level of the argument (for multi-level arguments)
        int toFlush = 0; // used to remove consumed elements from the raw input

        List<E> results = new ArrayList<>();

        for (String next : input) {
            buffer.append(next).append(" ");
            toFlush++;
            bufferMultilevel++;

            if (bufferMultilevel < range.getMin()) {
                continue;
            }

            if (!buffer.toString().contains(delimiter)) {
                if (range.getMax() == bufferMultilevel) {
                    break;
                }

                continue;
            }

            ParseResult<List<E>> parseResults = this.parseBufferWithDelimiter(buffer, delimiter, parser, argument, invocation);

            if (parseResults.isFailed()) {
                return ParseResult.failure(parseResults.getFailedReason());
            }

            results.addAll(parseResults.getSuccess());
            bufferMultilevel = 1;
        }

        rawInput.next(toFlush);

        buffer.deleteCharAt(buffer.length() - 1); // remove the last space

        int count = rawInput.size() + bufferMultilevel; // (+ level) because we need to add the last remaining elements from the buffer

        if (range.isBelowRange(count)) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
        }

        List<String> lastArguments = new ArrayList<>();

        lastArguments.addAll(StringUtil.spilt(buffer.toString(), RawCommand.COMMAND_SEPARATOR));
        lastArguments.addAll(rawInput.seeAll());

        RawInput lastInput = RawInput.of(lastArguments);

        ParseResult<E> lastResult = parser.parse(invocation, argument, lastInput);

        if (lastResult.isFailed()) {
            return ParseResult.failure(lastResult.getFailedReason());
        }

        results.add(lastResult.getSuccess());

        toFlush = Math.max(0, lastArguments.size() - lastInput.size() - bufferMultilevel);
        rawInput.next(toFlush);

        return ParseResult.success(results);
    }

    public ParseResult<List<E>> parseBufferWithDelimiter(StringBuilder buffer, String delimiter, Parser<SENDER, E> parser, Argument<E> argument, Invocation<SENDER> invocation) {
        int delimiterLength = delimiter.length();
        List<E> result = new ArrayList<>();

        do {
            int delimiterIndex = buffer.indexOf(delimiter);

            if (delimiterIndex == -1) {
                break;
            }

            String delimitedContent = buffer.substring(0, delimiterIndex);
            RawInput rawInput = RawInput.of(delimitedContent.split(RawCommand.COMMAND_SEPARATOR));
            ParseResult<E> parseResult = parser.parse(invocation, argument, rawInput);

            if (parseResult.isFailed()) {
                return ParseResult.failure(parseResult.getFailedReason());
            }

            result.add(parseResult.getSuccess());

            buffer.delete(0, delimiterIndex + delimiterLength);
        }
        while (buffer.length() > 0);

        return ParseResult.success(result);
    }

    abstract Collector<E, ?, ? extends COLLECTION> getCollector(CollectorArgument<COLLECTION> collectorArgument, Invocation<SENDER> invocation);

    @Override
    public Range getTypedRange(CollectorArgument<COLLECTION> argument) {
        return Range.moreThan(0);
    }

    @Override
    public SuggestionResult suggestTyped(Invocation<SENDER> invocation, CollectorArgument<COLLECTION> argument, SuggestionContext context) {
        return suggest(this.getElementType(argument, invocation), context, argument, invocation);
    }

    private <T> SuggestionResult suggest(Class<T> componentType, SuggestionContext context, CollectorArgument<COLLECTION> collectorArgument, Invocation<SENDER> invocation) {
        Argument<T> argument = new SimpleArgument<>(collectorArgument.getKeyName(), WrapFormat.notWrapped(componentType));

        ParserSet<SENDER, T> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, T> parser = parserSet.getValidParserOrThrow(invocation, argument);

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

    abstract protected Class<E> getElementType(CollectorArgument<COLLECTION> context, Invocation<SENDER> invocation);

}
