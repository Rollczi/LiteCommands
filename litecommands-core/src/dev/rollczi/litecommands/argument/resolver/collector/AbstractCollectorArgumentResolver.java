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
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.input.raw.RawInputView;
import dev.rollczi.litecommands.input.raw.RawInputViewLegacyAdapter;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCollectorArgumentResolver<SENDER, E, COLLECTION> extends TypedArgumentResolver<SENDER, COLLECTION, CollectorArgument<COLLECTION>> {

    private static final int BASE_ARGUMENT_COUNT = 1;
    private static final int NO_INDEX = -1;

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
        Collector<E, ?, ? extends COLLECTION> collector = getCollector(collectorArgument, invocation);

        if (parseResult.isFailed()) {
            COLLECTION empty = Stream.<E>empty().collect(collector);

            return ParseResult.conditional(empty, new SuccessRequirementCondition(parseResult.getFailedReason()));
        }

        COLLECTION result = parseResult.getSuccess().stream()
            .collect(collector);

        return ParseResult.success(result);
    }

    private ParseResult<List<E>> parseToList(Class<E> componentType, RawInput rawInput, CollectorArgument<COLLECTION> collectorArgument, Invocation<SENDER> invocation) {
        Argument<E> argument = new SimpleArgument<>(collectorArgument.getKeyName(), WrapFormat.notWrapped(componentType));
        ParserSet<SENDER, E> parserSet = parserRegistry.getParserSet(componentType, argument.getKey());
        Parser<SENDER, E> parser = parserSet.getValidParserOrThrow(invocation, argument);

        if (rawInput.hasNext() && rawInput.seeNext().isEmpty()) {
            String next = rawInput.next();

            if (parser.getRange(argument).isInRange(1)) {
                ParseResult<E> result = parser.parse(invocation, argument, RawInput.of(next));

                if (result.isSuccessful()) {
                    return ParseResult.success(Collections.singletonList(result.getSuccess()));
                }
            }

            return ParseResult.success(Collections.emptyList());
        }

        Range range = parser.getRange(argument);
        String delimiter = collectorArgument.getDelimiter();

        RawInputViewLegacyAdapter view = new RawInputViewLegacyAdapter(rawInput);
        ParseResult<List<E>> result = parseWithNoSpaceDelimiter(view, range, delimiter, invocation, argument, parser);

        if (result.isSuccessful()) {
            view.applyChanges();
        }

        return result;
    }

    private ParseResult<List<E>> parseWithNoSpaceDelimiter(RawInputView view, Range range, String delimiter, Invocation<SENDER> invocation, Argument<E> argument, Parser<SENDER, E> parser) {
        RawInputView elementView = this.findNextElementView(view, range, delimiter);
        List<E> results = new ArrayList<>();

        while (elementView != null) {
            String elementWithDelimiter = elementView.claim();
            String element = elementWithDelimiter.substring(0, elementWithDelimiter.length() - delimiter.length());
            List<String> arguments = StringUtil.spilt(element, RawCommand.COMMAND_SEPARATOR);

            if (range.isBelowRange(arguments.size())) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
            }

            ParseResult<E> parsedResult = parser.parse(invocation, argument, RawInput.of(arguments));

            if (parsedResult.isFailed()) {
                return ParseResult.failure(parsedResult.getFailedReason());
            }

            results.add(parsedResult.getSuccess());
            elementView = this.findNextElementView(view, range, delimiter);
        }

        int lastIndex = lastIndexOfInRange(view, range);

        if (lastIndex == -1) {
            if (results.isEmpty()) {
                return ParseResult.success(results);
            }

            return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
        }

        String rest = view.claim(0, lastIndex + 1);
        List<String> arguments = StringUtil.spilt(rest, RawCommand.COMMAND_SEPARATOR);
        ParseResult<E> parsedResult = parser.parse(invocation, argument, RawInput.of(arguments));

        if (parsedResult.isFailed()) {
            return ParseResult.failure(parsedResult.getFailedReason());
        }

        results.add(parsedResult.getSuccess());
        return ParseResult.success(results);
    }

    private int lastIndexOfInRange(RawInputView view, Range range) {
        String content = view.content();
        int index = NO_INDEX;

        for (int count = BASE_ARGUMENT_COUNT; count <= range.getMax(); count++) {
            int indexOf = content.indexOf(RawCommand.COMMAND_SEPARATOR_CHAR, index + 1);

            if (indexOf == NO_INDEX) {
                if (range.isBelowRange(count)) {
                    return NO_INDEX;
                }

                return content.length() - 1;
            }

            index = indexOf;
        }

        return index;
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

        Suggestion current = context.getCurrent();
        Range range = parser.getRange(argument);
        RawInput rawInput = RawInput.of(current.multilevelList());
        String delimiter = collectorArgument.getDelimiter();

        if (delimiter.equals(RawCommand.COMMAND_SEPARATOR)) {
            while (rawInput.hasNext()) {
                int count = rawInput.seeAll().size();

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

            return SuggestionResult.empty();
        }

        RawInputView fullView = RawInputView.of(current.multilevel());

        return this.suggestWithDelimiter(fullView, argument, suggester, parser, range, delimiter, context, invocation);
    }

    private <T> SuggestionResult suggestWithDelimiter(
        RawInputView fullView,
        Argument<T> argument,
        Suggester<SENDER, T> suggester,
        Parser<SENDER, T> parser,
        Range range,
        String delimiter,
        SuggestionContext context,
        Invocation<SENDER> invocation
    ) {
        Suggestion current = context.getCurrent();
        RawInputView elementView = this.findNextElementView(fullView, range, delimiter);

        if (elementView != null) {
            String elementWithDelimiter = elementView.claim();
            String element = elementWithDelimiter
                .substring(0, elementWithDelimiter.length() - delimiter.length());

            List<String> arguments = StringUtil.spilt(element, RawCommand.COMMAND_SEPARATOR);

            if (!range.isInRange(arguments.size())) {
                return SuggestionResult.empty(); // invalid argument count
            }

            ParseResult<T> parsedResult = parser.parse(invocation, argument, RawInput.of(arguments));

            if (parsedResult.isFailed()) {
                return SuggestionResult.empty();
            }

            return this.suggestWithDelimiter(fullView, argument, suggester, parser, range, delimiter, context, invocation);
        }

        int lastIndex = lastIndexOfInRange(fullView, range);

        if (lastIndex == -1) {
            lastIndex = fullView.length() - 1;
        }

        String rest = fullView.claim(0, lastIndex + 1);
        SuggestionContext suggestionContext = new SuggestionContext(rest);
        int beforeConsumed = suggestionContext.getConsumed();

        SuggestionResult suggestionResult = suggester.suggest(invocation, argument, suggestionContext);

        int afterConsumed = suggestionContext.getConsumed();

        if (afterConsumed < beforeConsumed) {
            throw new IllegalStateException("Suggester consumed more than before: " + afterConsumed + " < " + beforeConsumed + " for: " + argument.getKey() + " suggester: " + suggester.getClass().getName());
        }

        String base = current.multilevel();
        String baseWithoutRight = base.substring(0, base.length() - rest.length());
        SuggestionResult result = suggestionResult.appendLeftDirectly(baseWithoutRight);

        int count = suggestionContext.getCurrent().lengthMultilevel();

        if (range.isBelowRange(count)) {
            return result;
        }

        if (range.isInRange(count) && isValid(parser, argument, invocation, rest)) {
            result.add(Suggestion.of(base));

            if (!rest.isEmpty()) {
                result.add(Suggestion.of(base + delimiter));
            }
        }

        if (range.isAboveRange(count)) {
            int margin = count - range.getMax();
            context.setConsumed(context.getConsumed() - margin);
        }

        return result;
    }

    private <T> boolean isValid(Parser<SENDER, T> parser, Argument<T> argument, Invocation<SENDER> invocation, String rawArgument) {
        return parser.matchParse(invocation, argument, RawInput.of(rawArgument));
    }

    @Nullable
    private RawInputView findNextElementView(RawInputView rawInputView, Range argumentRange, String delimiter) {
        int min = argumentRange.getMin();
        int max = argumentRange.getMax();
        int delimiterLength = delimiter.length();

        int lastDelimiterIndex = NO_INDEX;

        do {
            lastDelimiterIndex = rawInputView.indexOf(delimiter, lastDelimiterIndex == NO_INDEX ? 0 : lastDelimiterIndex + delimiterLength);

            if (lastDelimiterIndex == NO_INDEX) {
                return null;
            }

            lastDelimiterIndex += delimiterLength;

            RawInputView sub = rawInputView.sub(0, lastDelimiterIndex);
            RawInputView subWithoutDelimiter = rawInputView.sub(0, lastDelimiterIndex - delimiterLength);
            int argumentCount = BASE_ARGUMENT_COUNT + subWithoutDelimiter.countOf(RawCommand.COMMAND_SEPARATOR_CHAR);

            if (argumentCount > max) {
                return null;
            }

            if (argumentCount >= min) {
                return sub;
            }
        } while (lastDelimiterIndex != NO_INDEX);

        return null;
    }

    abstract protected Class<E> getElementType(CollectorArgument<COLLECTION> context, Invocation<SENDER> invocation);

    private static class SuccessRequirementCondition implements RequirementCondition {
        private final Object failedReason;

        private SuccessRequirementCondition(Object failedReason) {
            this.failedReason = failedReason;
        }

        @Override
        public Optional<FailedReason> check(Invocation<?> ignored, CommandExecutorMatchResult result) {
            if (result.isSuccessful()) {
                return Optional.empty();
            }

            return Optional.of(FailedReason.of(failedReason));
        }
    }

}
