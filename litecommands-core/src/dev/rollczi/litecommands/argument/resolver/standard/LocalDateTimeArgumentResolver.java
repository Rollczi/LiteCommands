package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static dev.rollczi.litecommands.argument.resolver.standard.TemporalAccessorUtils.allDaysOfWeek;
import java.util.regex.Pattern;

public class LocalDateTimeArgumentResolver<SENDER> extends TemporalAccessorArgumentResolver<SENDER, LocalDateTime> {

    private static final Pattern PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault());

    public LocalDateTimeArgumentResolver(MessageRegistry<SENDER> messageRegistry) {
        super(
            messageRegistry,
            LiteMessages.INSTANT_INVALID_FORMAT,
            FORMATTER,
            temporal -> LocalDateTime.from(temporal),
            () -> allDaysOfWeek(LocalDateTime.now()),
            PATTERN
        );
    }

}
