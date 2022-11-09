package dev.rollczi.litecommands.argument.basictype.time;

import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class JapaneseEraArgument extends TemporalAccessorArgument<JapaneseEra> {

    private static final char ERA_YEAR_SEPARATOR = ' ';
    private static final DateTimeFormatter JAPANESE_DATE_FORMATTER = new DateTimeFormatterBuilder()
        .appendText(ChronoField.ERA)
        .appendLiteral(ERA_YEAR_SEPARATOR)

        .toFormatter(Locale.JAPAN)
        .withChronology(JapaneseChronology.INSTANCE);

    private static final TemporalQuery<JapaneseEra> JAPANESE_DATE_QUERY = temporal -> temporal.query(JapaneseDate::from).getEra();
    private static final Supplier<List<JapaneseEra>> JAPANESE_ERAS_SUPPLIER = () -> Arrays.asList(JapaneseEra.values());

    public JapaneseEraArgument() {
        super(JAPANESE_DATE_FORMATTER, JAPANESE_DATE_QUERY, JAPANESE_ERAS_SUPPLIER);
    }

}
