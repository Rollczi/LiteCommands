package dev.rollczi.litecommands.argument.resolver.standard;

import static dev.rollczi.litecommands.argument.resolver.standard.TemporalAccessorUtils.allDaysOfWeek;
import static dev.rollczi.litecommands.message.LiteMessages.INSTANT_INVALID_FORMAT;
import static java.time.ZoneOffset.UTC;

import dev.rollczi.litecommands.message.MessageRegistry;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class InstantArgumentResolver<SENDER> extends TemporalAccessorArgumentResolver<SENDER, Instant> {

    private static final Pattern INSTANT_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(UTC);

    public InstantArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        super(
            messageRegistry,
            INSTANT_INVALID_FORMAT,
            FORMATTER,
            Instant::from,
            () -> allDaysOfWeek(Instant.now()),
            INSTANT_PATTERN
        );
    }

}
