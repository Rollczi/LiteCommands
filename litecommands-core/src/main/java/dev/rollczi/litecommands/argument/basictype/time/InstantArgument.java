package dev.rollczi.litecommands.argument.basictype.time;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InstantArgument extends TemporalAccessorArgument<Instant> {

    public InstantArgument() {
        super(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT).withZone(ZoneOffset.UTC),
            Instant::from,
            () -> TemporalUtils.allDaysOfWeek(Instant.now())
        );
    }

}
