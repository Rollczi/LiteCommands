package dev.rollczi.litecommands.argument.basictype.time;

import java.time.Month;
import java.time.MonthDay;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonthDayArgument extends TemporalAccessorArgument<MonthDay> {

    public MonthDayArgument() {
        super("MM-dd",
            MonthDay::from,
            () -> Stream.of(Month.values())
                .map(month -> MonthDay.of(month, 1))
                .collect(Collectors.toList())
        );
    }

}
