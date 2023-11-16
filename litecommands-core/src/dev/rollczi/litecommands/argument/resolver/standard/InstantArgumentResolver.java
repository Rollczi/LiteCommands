package dev.rollczi.litecommands.argument.resolver.standard;

import static dev.rollczi.litecommands.argument.resolver.standard.TemporalAccessorUtils.allDaysOfWeek;
import static dev.rollczi.litecommands.message.LiteMessages.INSTANT_INVALID_FORMAT;
import static java.time.ZoneOffset.UTC;

import dev.rollczi.litecommands.message.MessageRegistry;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantArgumentResolver<SENDER> extends TemporalAccessorArgumentResolver<SENDER, Instant> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(UTC);

    public InstantArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        super(
            messageRegistry,
            INSTANT_INVALID_FORMAT,
            FORMATTER,
            Instant::from,
            () -> allDaysOfWeek(Instant.now())
        );
    }
}
