package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.profile.ProfiledMultipleArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseCompletedResult;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
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
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.util.FutureUtil;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCollectorArgumentResolver<SENDER, COLLECTION> extends ProfiledMultipleArgumentResolver<SENDER, COLLECTION, VarargsProfile> {

    private static final int BASE_ARGUMENT_COUNT = 1;
    private static final int NO_INDEX = -1;

    private final ParserRegistry<SENDER> parserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;

    protected AbstractCollectorArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(VarargsProfile.NAMESPACE);
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
    }

    @Override
    protected ParseResult<COLLECTION> parse(Invocation<SENDER> invocation, Argument<COLLECTION> context, RawInput rawInput, VarargsProfile collectionArgument) {
        return parse(this.getElementType(collectionArgument), rawInput, context, collectionArgument, invocation);
    }

    private <E> ParseResult<COLLECTION> parse(TypeToken<E> componentType, RawInput rawInput, Argument<COLLECTION> argument, VarargsProfile collectionArgument, Invocation<SENDER> invocation) {
        Collector<E, ?, ? extends COLLECTION> collector = getCollector(collectionArgument, invocation);

        return parseToList(componentType, rawInput, argument, collectionArgument, invocation)
            .map(list -> (COLLECTION) list.stream().collect(collector))
            .mapFailure(failedReason -> {
                COLLECTION empty = Stream.<E>empty().collect(collector);

                return ParseResult.conditional(empty, new SuccessRequirementCondition(failedReason));
            });
    }

    @Override
    public boolean match(Invocation<SENDER> invocation, Argument<COLLECTION> context, RawInput input, VarargsProfile collectionArgument) {
        return match0(invocation, context, input, collectionArgument);
    }

    private <E> boolean match0(Invocation<SENDER> invocation, Argument<COLLECTION> collectionArgument, RawInput input, VarargsProfile collectionArgumentContainer) {
        if (input.hasNext() && input.seeNext().isEmpty()) {
            input.next();
            return true;
        }

        TypeToken<E> elementType = this.getElementType(collectionArgumentContainer);
        Argument<E> argument = collectionArgument.child(elementType);
        Parser<SENDER, E> parser = parserRegistry.getParser(argument);
        Range range = parser.getRange(argument);

        RawInputViewLegacyAdapter view = new RawInputViewLegacyAdapter(input);
        ParseCompletedResult<List<List<String>>> result = findRawValues(view, range, collectionArgumentContainer.getDelimiter());

        if (result.isFailed()) {
            return false;
        }

        boolean isAllMatch = result.getSuccess().stream()
            .allMatch(rawResult -> parser.match(invocation, argument, RawInput.of(rawResult)));

        if (isAllMatch) {
            view.applyChanges(); // we need to apply changes to the input for next suggestions
        }

        return true; // because we don't want to stop a suggestion (collection can be empty)
    }

    private <E> ParseResult<List<E>> parseToList(TypeToken<E> componentType, RawInput rawInput, Argument<COLLECTION> collectionArgument, VarargsProfile collectorArgumentHolder, Invocation<SENDER> invocation) {
        Argument<E> argument = collectionArgument.child(componentType);

        Parser<SENDER, E> parser = parserRegistry.getParser(argument);

        if (rawInput.hasNext() && rawInput.seeNext().isEmpty()) {
            String next = rawInput.next();

            if (parser.getRange(argument).isInRange(1)) {
                return parser.parse(invocation, argument, RawInput.of(next))
                    .map(elementResult -> Collections.singletonList(elementResult))
                    .mapFailure(failedReason -> ParseResult.success(Collections.emptyList()));
            }

            return ParseResult.success(Collections.emptyList());
        }

        Range range = parser.getRange(argument);
        String delimiter = collectorArgumentHolder.getDelimiter();

        RawInputViewLegacyAdapter view = new RawInputViewLegacyAdapter(rawInput);

        return this.findRawValues(view, range, delimiter)
            .flatMap(rawResults -> ParseResult.completableFuture(this.parseRawResults(rawResults, invocation, argument, parser)))
            .whenSuccessful(list -> view.applyChanges());
    }

    private ParseCompletedResult<List<List<String>>> findRawValues(RawInputView view, Range range, String delimiter) {
        RawInputView elementView = this.findNextElementView(view, range, delimiter);
        List<List<String>> rawResults = new ArrayList<>();

        while (elementView != null) {
            String elementWithDelimiter = elementView.claim();
            String element = elementWithDelimiter.substring(0, elementWithDelimiter.length() - delimiter.length());
            List<String> arguments = StringUtil.splitBySpace(element);

            if (range.isBelowRange(arguments.size())) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
            }

            rawResults.add(arguments);
            elementView = this.findNextElementView(view, range, delimiter);
        }

        int lastIndex = lastIndexOfInRange(view, range);

        if (lastIndex == -1) {
            if (rawResults.isEmpty()) {
                return ParseResult.success(Collections.emptyList());
            }

            return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
        }

        String rest = view.claim(0, lastIndex + 1);
        List<String> arguments = StringUtil.splitBySpace(rest);
        rawResults.add(arguments);

        return ParseResult.success(rawResults);
    }

    private <E> CompletableFuture<ParseCompletedResult<List<E>>> parseRawResults(List<List<String>> rawResults, Invocation<SENDER> invocation, Argument<E> argument, Parser<SENDER, E> parser) {
        List<CompletableFuture<ParseCompletedResult<E>>> futures = rawResults.stream()
            .map(rawResult -> parser.parse(invocation, argument, RawInput.of(rawResult)).asFuture())
            .collect(Collectors.toList());

        return FutureUtil.asList(futures).thenApply(requirementResults -> {
            for (ParseCompletedResult<E> result : requirementResults) {
                if (result.isFailed()) {
                    return ParseResult.failure(result.getFailedReason());
                }
            }

            List<E> results = requirementResults.stream()
                .map(result -> result.getSuccess())
                .collect(Collectors.toList());

            return ParseResult.success(results);
        });
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

    abstract <E> Collector<E, ?, ? extends COLLECTION> getCollector(VarargsProfile collectionArgument, Invocation<SENDER> invocation);

    @Override
    public Range getRange(Argument<COLLECTION> argument, VarargsProfile varargsProfile) {
        return Range.moreThan(0);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<COLLECTION> argument, SuggestionContext context, VarargsProfile varargsProfile) {
        return suggest(this.getElementType(varargsProfile), context, argument, varargsProfile, invocation);
    }

    private <T> SuggestionResult suggest(TypeToken<T> componentType, SuggestionContext context, Argument<COLLECTION> collectionArgument, VarargsProfile varargsProfile, Invocation<SENDER> invocation) {
        Argument<T> argument = collectionArgument.child(componentType);

        Parser<SENDER, T> parser = parserRegistry.getParser(argument);
        Suggester<SENDER, T> suggester = suggesterRegistry.getSuggester(componentType.getRawType(), argument.getKey());

        Suggestion current = context.getCurrent();
        Range range = parser.getRange(argument);
        String delimiter = varargsProfile.getDelimiter();
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

            List<String> arguments = StringUtil.splitBySpace(element);

            if (!range.isInRange(arguments.size())) {
                return SuggestionResult.empty(); // invalid argument count
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

        if (range.isInRange(count) && this.isMatch(parser, argument, invocation, rest)) {
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

    private <T> boolean isMatch(Parser<SENDER, T> parser, Argument<T> argument, Invocation<SENDER> invocation, String rawArgument) {
        return parser.match(invocation, argument, RawInput.of(StringUtil.splitBySpace(rawArgument)));
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

    protected <E> TypeToken<E> getElementType(VarargsProfile varargsProfile) {
        return (TypeToken<E>) varargsProfile.getElementType();
    }

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
