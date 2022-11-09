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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JapaneseDateArgument extends TemporalAccessorArgument<JapaneseDate> {

    private static final char ERA_YEAR_SEPARATOR = ' ';
    private static final char JAPANESE_YEAR_SYMBOL = '\u5e74';
    private static final char JAPANESE_MONTH_SYMBOL = '\u6708';
    private static final char JAPANESE_DAY_SYMBOL = '\u65e5';

    private static final DateTimeFormatter NATIVE_JAPANESE_DATE_FORMATTER = new DateTimeFormatterBuilder()
        .appendText(ChronoField.ERA)
        .appendLiteral(ERA_YEAR_SEPARATOR)

        .appendValue(ChronoField.YEAR_OF_ERA)
        .appendLiteral(JAPANESE_YEAR_SYMBOL)

        .appendValue(ChronoField.MONTH_OF_YEAR)
        .appendLiteral(JAPANESE_MONTH_SYMBOL)

        .appendValue(ChronoField.DAY_OF_MONTH)
        .appendLiteral(JAPANESE_DAY_SYMBOL)

        .toFormatter(Locale.JAPAN)
        .withChronology(JapaneseChronology.INSTANCE);

    private static final DateTimeFormatter ISO_JAPANESE_DATE_FORMATTER = DateTimeFormatter.ofPattern("GGGG yyyy-MM-dd", Locale.JAPAN)
        .withChronology(JapaneseChronology.INSTANCE);

    private static final TemporalQuery<JapaneseDate> JAPANESE_DATE_QUERY = JapaneseDate::from;

    private static final int SUPPORTED_MEIJI_ERA_YEAR = 6;
    private static final List<JapaneseDate> FIRST_DAY_OF_EACH_ERA = Arrays.asList(
        JapaneseDate.of(JapaneseEra.MEIJI, SUPPORTED_MEIJI_ERA_YEAR, 1, 1),
        JapaneseDate.of(JapaneseEra.TAISHO, 1, 7, 30),
        JapaneseDate.of(JapaneseEra.SHOWA, 1, 12, 25),
        JapaneseDate.of(JapaneseEra.HEISEI, 1, 1, 8)
    );

    private static final Supplier<List<JapaneseDate>> JAPANESE_DATE_SUPPLIER = () -> Stream.concat(FIRST_DAY_OF_EACH_ERA.stream(), Stream.of(JapaneseDate.now()))
        .collect(Collectors.toList());

    protected JapaneseDateArgument(DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter, JAPANESE_DATE_QUERY, JAPANESE_DATE_SUPPLIER);
    }

    protected JapaneseDateArgument(DateTimeFormatter dateTimeFormatter, Supplier<List<JapaneseDate>> suggestionsSupplier) {
        super(dateTimeFormatter, JAPANESE_DATE_QUERY, suggestionsSupplier);
    }

    public static JapaneseDateArgument nativeFormat() {
        return new JapaneseDateArgument(NATIVE_JAPANESE_DATE_FORMATTER);
    }

    public static JapaneseDateArgument isoFormat() {
        return new JapaneseDateArgument(ISO_JAPANESE_DATE_FORMATTER);
    }

}
