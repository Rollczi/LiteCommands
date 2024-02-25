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
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.ArrayList;
import java.util.Arrays;
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
        Argument<E> argument = new SimpleArgument<>(collectorArgument.getKeyName(), WrapFormat.notWrapped(componentType));

        ParserSet<SENDER, E> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, E> parser = parserSet.getValidParserOrThrow(invocation, argument);
        Range range = parser.getRange(argument);
        String delimiter = collectorArgument.getDelimiter();

        List<E> values = new ArrayList<>();

        // If the delimiter is a space
        if (delimiter.equals(RawCommand.COMMAND_SEPARATOR)) {
            while (rawInput.hasNext()) {
                int count = rawInput.seeAll().size();

                if (range.isBelowRange(count)) {
                    return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
                }

                ParseResult<E> parsedResult = parser.parse(invocation, argument, rawInput);

                if (parsedResult.isFailed()) {
                    return ParseResult.failure(parsedResult.getFailedReason());
                }

                values.add(parsedResult.getSuccess());
            }
        }

        // If the delimiter is not a space
        else {
            List<String> input = new ArrayList<>(rawInput.seeAll());
            StringBuilder buffer = new StringBuilder();
            int current = 0;
            int level = 0;

            for (int i = 0; i < input.size(); i++) {
                String next = input.get(i);

                if (i != 0) {
                    buffer.append(RawCommand.COMMAND_SEPARATOR);
                }

                buffer.append(next);
                current++;
                level++;

                if (level < range.getMin()) {
                    continue;
                }

                if (!buffer.toString().contains(delimiter)) {
                    if (range.getMin() == level) {
                        break;
                    }

                    continue;
                }

                ParseResult<List<E>> parseResult = process(delimiter, buffer, range.getMin(), invocation, argument, parser);

                if (parseResult.isFailed()) {
                    return ParseResult.failure(parseResult.getFailedReason());
                }

                values.addAll(parseResult.getSuccess());

                for (int j = 0; j < current; j++) {
                    rawInput.next();
                }

                current = 0;
                level = 1;
            }

            for (int j = 0; j < current; j++) {
                rawInput.next();
            }

            if (!input.isEmpty()) {
                int count = rawInput.size() + level; // +level because we need to add the last element from the buffer

                if (range.isBelowRange(count)) {
                    return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
                }

                List<String> list = new ArrayList<>();

                list.addAll(Arrays.asList(buffer.toString().split(RawCommand.COMMAND_SEPARATOR)));
                list.addAll(rawInput.seeAll());

                RawInput currentInput = RawInput.of(list);

                ParseResult<E> parseResult = parser.parse(invocation, argument, currentInput);

                if (parseResult.isFailed()) {
                    return ParseResult.failure(parseResult.getFailedReason());
                }

                values.add(parseResult.getSuccess());

                int toPurge = Math.max(0, list.size() - currentInput.size() - level);

                for (int i = 0; i < toPurge; i++) {
                    rawInput.next();
                }
            }
        }


        Collector<E, ?, ? extends COLLECTION> collector = getCollector(collectorArgument, invocation);
        COLLECTION result = values.stream().collect(collector);

        return ParseResult.success(result);
    }

    public ParseResult<List<E>> process(String delimiter, StringBuilder buffer, int range, Invocation<SENDER> invocation, Argument<E> argument, Parser<SENDER, E> parser) {
        int length = delimiter.length();
        List<E> result = new ArrayList<>();

        do {
            int index = buffer.indexOf(delimiter);

            if (index == -1) {
                break;
            }

            String substring = buffer.substring(0, index);
            RawInput rawInput = RawInput.of(substring.split(RawCommand.COMMAND_SEPARATOR));
            ParseResult<E> parseResult = parser.parse(invocation, argument, rawInput);

            if (parseResult.isFailed()) {
                return ParseResult.failure(parseResult.getFailedReason());
            }

            result.add(parseResult.getSuccess());

            buffer.delete(0, index + length);
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
