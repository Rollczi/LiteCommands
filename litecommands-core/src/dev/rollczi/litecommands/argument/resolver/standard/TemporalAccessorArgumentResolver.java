package dev.rollczi.litecommands.argument.resolver.standard;

import static dev.rollczi.litecommands.argument.parser.ParseResult.failure;
import static dev.rollczi.litecommands.argument.parser.ParseResult.success;
import static dev.rollczi.litecommands.input.raw.RawCommand.COMMAND_SEPARATOR;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.List;
import java.util.function.Supplier;

class TemporalAccessorArgumentResolver<SENDER, UNIT extends TemporalAccessor> implements MultipleArgumentResolver<SENDER, UNIT> {

    private static final String ARGUMENT_SEPARATOR = " ";
    private static final String FORMATTER_ELEMENT_SEPARATOR = " ";
    private final MessageRegistry<SENDER> messageRegistry;
    private final MessageKey<String> invalidFormatMessage;
    private final DateTimeFormatter formatter;
    private final TemporalQuery<UNIT> query;
    private final Supplier<List<UNIT>> suggestions;
    private final int argumentCount;

    protected TemporalAccessorArgumentResolver(
        MessageRegistry<SENDER> messageRegistry,
        MessageKey<String> invalidFormatMessage,
        DateTimeFormatter formatter,
        TemporalQuery<UNIT> query,
        Supplier<List<UNIT>> suggestions) {
        this.messageRegistry = messageRegistry;
        this.invalidFormatMessage = invalidFormatMessage;
        this.formatter = formatter;
        this.query = query;
        this.suggestions = suggestions;
        this.argumentCount = getElementCount(formatter);
    }

    protected TemporalAccessorArgumentResolver(
        MessageRegistry<SENDER> messageRegistry,
        MessageKey<String> invalidFormatMessage,
        String formatterPattern,
        TemporalQuery<UNIT> query,
        Supplier<List<UNIT>> suggestions) {
        this(messageRegistry, invalidFormatMessage, DateTimeFormatter.ofPattern(formatterPattern), query, suggestions);
    }

    @Override
    public ParseResult<UNIT> parse(Invocation<SENDER> invocation, Argument<UNIT> argument, RawInput rawInput) {
        String commandInput = String.join(COMMAND_SEPARATOR, rawInput.seeNext(this.argumentCount));
        try {
            String rawInstant = String.join(ARGUMENT_SEPARATOR, rawInput.next(this.argumentCount));
            return success(this.formatter.parse(rawInstant, query));
        } catch (DateTimeException exception) {
            return failure(this.messageRegistry.getInvoked(this.invalidFormatMessage, invocation, commandInput));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<UNIT> argument, SuggestionContext context) {
        return this.suggestions.get().stream()
            .map(this.formatter::format)
            .collect(SuggestionResult.collector());
    }

    @Override
    public Range getRange(Argument<UNIT> unitArgument) {
        return Range.of(this.argumentCount);
    }

    private int getElementCount(DateTimeFormatter formatter) {
        return formatter.toString().split(FORMATTER_ELEMENT_SEPARATOR).length;
    }
}
