package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.shared.EstimatedTemporalAmountParser;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class DurationArgument extends TemporalAmountArgument<Duration> {

    static final List<Duration> SUGGESTED_DURATIONS = Arrays.asList(
        Duration.ofSeconds(1),
        Duration.ofSeconds(5),
        Duration.ofSeconds(10),
        Duration.ofSeconds(30),
        Duration.ofMinutes(1),
        Duration.ofMinutes(1).plus(Duration.ofSeconds(30)),
        Duration.ofMinutes(5),
        Duration.ofMinutes(10),
        Duration.ofMinutes(30),
        Duration.ofHours(1),
        Duration.ofHours(5),
        Duration.ofHours(10),
        Duration.ofDays(1),
        Duration.ofDays(7),
        Duration.ofDays(30)
    );

    public DurationArgument() {
        super(
            EstimatedTemporalAmountParser.DATE_TIME_UNITS,
            () -> SUGGESTED_DURATIONS
        );
    }

}
