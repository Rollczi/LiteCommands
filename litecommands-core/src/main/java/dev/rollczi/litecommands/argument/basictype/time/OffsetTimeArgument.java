package dev.rollczi.litecommands.argument.basictype.time;

import java.time.OffsetTime;

public class OffsetTimeArgument extends TemporalAccessorArgument<OffsetTime> {

    public OffsetTimeArgument() {
        super("HH:mm:ss xxx",
            OffsetTime::from,
            () -> TemporalUtils.allHoursOfDay(OffsetTime.now())
        );
    }

}
