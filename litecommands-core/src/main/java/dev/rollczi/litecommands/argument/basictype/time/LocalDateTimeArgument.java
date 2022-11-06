package dev.rollczi.litecommands.argument.basictype.time;

import java.time.LocalDateTime;

public class LocalDateTimeArgument extends TemporalAccessorArgument<LocalDateTime> {

    public LocalDateTimeArgument() {
        super("yyyy-MM-dd HH:mm:ss",
            LocalDateTime::from,
            () -> TemporalUtils.allDaysOfWeek(LocalDateTime.now())
        );
    }

}
