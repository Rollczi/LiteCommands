package dev.rollczi.litecommands.argument.basictype.time;

import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JapaneseDateArgument extends TemporalAccessorArgument<JapaneseDate> {

    public JapaneseDateArgument() {
        super(DateTimeFormatter.ofPattern("GGGG yyyy-MM-dd", Locale.ROOT).withChronology(JapaneseChronology.INSTANCE),
            JapaneseDate::from,
            () -> TemporalUtils.allDaysOfWeek(JapaneseDate.now())
        );
    }

}
