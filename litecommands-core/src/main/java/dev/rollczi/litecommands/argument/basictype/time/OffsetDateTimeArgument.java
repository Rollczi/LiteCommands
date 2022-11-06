package dev.rollczi.litecommands.argument.basictype.time;

import java.time.OffsetDateTime;

public class OffsetDateTimeArgument extends TemporalAccessorArgument<OffsetDateTime> {

    public OffsetDateTimeArgument() {
        super("yyyy-MM-dd HH:mm:ss xxx",
            OffsetDateTime::from,
            () -> TemporalUtils.allDaysOfWeek(OffsetDateTime.now())
        );
    }

}
