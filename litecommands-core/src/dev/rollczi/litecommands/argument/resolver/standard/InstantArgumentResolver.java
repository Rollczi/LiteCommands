package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstantArgumentResolver<SENDER> implements MultipleArgumentResolver<SENDER, Instant> {

    private final DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC);

    private final MessageRegistry<SENDER> messageRegistry;

    public InstantArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ParseResult<Instant> parse(Invocation<SENDER> invocation, Argument<Instant> argument, RawInput rawInput) {
        final String commandInput = String.join(RawCommand.COMMAND_SEPARATOR, rawInput.seeNext(2));

        try {
            final String dateInput = String.join(" ", rawInput.next(2));
            return ParseResult.success(Instant.from(formatter.parse(dateInput)));
        } catch (DateTimeException ignored) {
            return ParseResult.failure(this.messageRegistry.getInvoked(LiteMessages.INSTANT_INVALID_FORMAT, invocation, commandInput));
        }
    }

    @Override
    public Range getRange(Argument<Instant> instantArgument) {
        return Range.of(2);
    }
}
