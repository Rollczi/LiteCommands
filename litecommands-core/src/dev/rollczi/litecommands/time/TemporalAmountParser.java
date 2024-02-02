package dev.rollczi.litecommands.time;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * This class is used to parse temporal amount from string and format it to string.
 * It can parse both {@link Period} and {@link Duration}.
 * <p>
 *     It can parse both estimated and exact temporal amount.
 *     Estimated temporal amount is a temporal amount that is not exact.
 *     For example, if you want to parse 1 month, you can write 1mo or 30d.
 *     If you write 1mo, it can be estimated to 28, 29, 30 or 31 days.
 *     If you write 1y, it can be estimated to 365 or 366 days.
 * </p>
 * <p>
 *     This class is immutable.
 *     You can create new instance using constructor {@link PeriodParser#PeriodParser()} or {@link DurationParser#DurationParser()}.
 *     You can also use {@link DurationParser#DATE_TIME_UNITS}, {@link PeriodParser#DATE_UNITS} and {@link DurationParser#TIME_UNITS}.
 *     You can add new units using {@link #withUnit(String, ChronoUnit)}.
 * </p>
 * <p>
 *     Use {@link #parse(String)} to parse temporal amount from string.
 *     Use {@link #format(TemporalAmount)} to format temporal amount to string.
 * </p>
 * @param <T> type of temporal amount (must be {@link Duration} or {@link Period} or subclass of TemporalAmount)
 */
public abstract class TemporalAmountParser<T extends TemporalAmount> {

    private static final Map<ChronoUnit, Long> UNIT_TO_NANO = new LinkedHashMap<>();
    private static final Map<ChronoUnit, Integer> PART_TIME_UNITS = new LinkedHashMap<>();

    static {
        UNIT_TO_NANO.put(ChronoUnit.NANOS, 1L);
        UNIT_TO_NANO.put(ChronoUnit.MICROS, 1_000L);
        UNIT_TO_NANO.put(ChronoUnit.MILLIS, 1_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.SECONDS, 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.MINUTES, 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.HOURS, 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.DAYS, 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.WEEKS, 7 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.MONTHS, 30 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.YEARS, 365 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.DECADES, 10 * 365 * 24 * 60 * 60 * 1_000_000_000L);

        PART_TIME_UNITS.put(ChronoUnit.NANOS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.MICROS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.MILLIS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.SECONDS, 60);
        PART_TIME_UNITS.put(ChronoUnit.MINUTES, 60);
        PART_TIME_UNITS.put(ChronoUnit.HOURS, 24);
        PART_TIME_UNITS.put(ChronoUnit.DAYS, 7);
        PART_TIME_UNITS.put(ChronoUnit.WEEKS, 4);
        PART_TIME_UNITS.put(ChronoUnit.MONTHS, 12);
        PART_TIME_UNITS.put(ChronoUnit.YEARS, Integer.MAX_VALUE);
    }

    private final Map<String, ChronoUnit> units = new LinkedHashMap<>();
    private final LocalDateTimeProvider baseForTimeEstimation;

    protected TemporalAmountParser(LocalDateTimeProvider baseForTimeEstimation) {
        this.baseForTimeEstimation = baseForTimeEstimation;
    }

    protected TemporalAmountParser(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        this.baseForTimeEstimation = baseForTimeEstimation;
        this.units.putAll(units);
    }

    public TemporalAmountParser<T> withUnit(String symbol, ChronoUnit chronoUnit) {
        if (this.units.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol " + symbol + " is already used");
        }

        if (!this.validCharacters(symbol, Character::isLetter)) {
            throw new IllegalArgumentException("Symbol " + symbol + " contains non-letter characters");
        }

        Map<String, ChronoUnit> newUnits = new LinkedHashMap<>(this.units);
        newUnits.put(symbol, chronoUnit);
        return clone(newUnits, this.baseForTimeEstimation);
    }

    public TemporalAmountParser<T> withLocalDateTimeProvider(LocalDateTimeProvider baseForTimeEstimation) {
        return clone(this.units, baseForTimeEstimation);
    }

    protected abstract TemporalAmountParser<T> clone(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation);

    private boolean validCharacters(String content, Predicate<Character> predicate) {
        for (int i = 0; i < content.length(); i++) {
            if (predicate.test(content.charAt(i))) {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Parses the given string and returns the estimated temporal amount.]
     * <p>
     *     Examples:
     *     <ul>
     *         <li>{@code 1ns} - 1 nanosecond</li>
     *         <li>{@code 1us} - 1 microsecond</li>
     *         <li>{@code 1ms} - 1 millisecond</li>
     *         <li>{@code 1s} - 1 second</li>
     *         <li>{@code 1m} - 1 minute</li>
     *         <li>{@code 1h} - 1 hour</li>
     *         <li>{@code 1d} - 1 day</li>
     *         <li>{@code 1w} - 1 week</li>
     *         <li>{@code 1mo} - 1 month</li>
     *         <li>{@code 1y} - 1 year</li>
     *         <li>{@code 1d2h} - 1 day and 2 hours</li>
     *         <li>{@code 1d2h3m} - 1 day, 2 hours and 3 minutes</li>
     *         <li>{@code 1d2h3m4s} - 1 day, 2 hours, 3 minutes and 4 seconds</li>
     *         <li>{@code 1y2mo3w4d5h6m7s} - 1 year, 2 months, 3 weeks, 4 days, 5 hours, 6 minutes and 7 seconds</li>
     *     </ul>
     *
     * @param input the input to parse. Must not be null or empty. Must be in the format of {@code <number><unit>}.
     *              The string can not contain any characters that are not part of the number or the unit.
     *              The only exception is the minus sign, which is allowed at the start of the string.
     *              The unit must be one of the units that are configured for this parser.
     *              The number must be a valid number that can be parsed by {@link Long#parseLong(String)}.
     *
     * @return the estimated temporal amount
     * @throws IllegalArgumentException if the input is null or empty,
     * if the input is not in the format of {@code <number><unit>},
     * if the unit is not a valid unit,
     * if the number is not a valid number,
     * if the number is decimal
     */
    public T parse(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }

        T total = this.getZero();
        boolean negative = false;

        StringBuilder number = new StringBuilder();
        StringBuilder unit = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '-') {
                if (i != 0) {
                    throw new IllegalArgumentException("Minus sign is only allowed at the start of the input");
                }

                negative = true;
                continue;
            }

            if (Character.isDigit(c)) {
                number.append(c);
                continue;
            }

            if (Character.isLetter(c)) {
                unit.append(c);
            }
            else {
                throw new IllegalArgumentException("Invalid character " + c + " in input");
            }

            if (i == input.length() - 1 || Character.isDigit(input.charAt(i + 1))) {
                TemporalEntry temporalEntry = this.parseTemporal(number.toString(), unit.toString());

                total = this.plus(this.baseForTimeEstimation, total, temporalEntry);

                number.setLength(0);
                unit.setLength(0);
            }
        }

        if (number.length() > 0 || unit.length() > 0) {
            throw new IllegalArgumentException("Input is not in the format of <number><unit>");
        }

        if (negative) {
            total = this.negate(total);
        }

        return total;
    }

    protected abstract T plus(LocalDateTimeProvider baseForTimeEstimation, T temporalAmount, TemporalEntry temporalEntry);

    protected abstract T negate(T temporalAmount);

    protected abstract T getZero();

    private TemporalEntry parseTemporal(String number, String unit) {
        if (number.isEmpty()) {
            throw new IllegalArgumentException("Missing number before unit " + unit);
        }

        ChronoUnit chronoUnit = this.units.get(unit);

        if (chronoUnit == null) {
            throw new IllegalArgumentException("Unknown unit " + unit);
        }

        try {
            long count = Long.parseLong(number);

            return new TemporalEntry(count, chronoUnit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number " + number);
        }
    }

    protected static class TemporalEntry {
        private final long count;
        private final ChronoUnit unit;

        public TemporalEntry(long count, ChronoUnit unit) {
            this.count = count;
            this.unit = unit;
        }

        public long getCount() {
            return count;
        }

        public ChronoUnit getUnit() {
            return unit;
        }
    }

    /**
     * Formats the given estimated temporal amount to a string.
     * <p>
     *     Examples:
     * </p>
     * <ul>
     *     <li>Duration of 30 seconds: {@code 30s}</li>
     *     <li>Duration of 25 hours: {@code 1d1h}</li>
     *     <li>Duration of 1 year, 2 months, 3 weeks, 4 days, 5 hours, 6 minutes and 7 seconds: {@code 1y2mo3w4d5h6m7s}</li>
     *     <li>Duration of 1 hours and 61 minutes: {@code 2h1m}</li>
     *     <li>Past duration of 1 hours and 61 minutes: {@code -2h1m}</li>
     *     <li>Period of 1 year, 2 months, 4 days: {@code 1y2mo4d}</li>
     *     <li>Past period of 1 year, 2 months, 4 days: {@code -1y2mo4d}</li>
     * </ul>
     *
     * @param temporalAmount the temporal amount to format. Must not be null.
     * @return the formatted string
     */
    public String format(T temporalAmount) {
        StringBuilder builder = new StringBuilder();
        Duration duration = this.toDuration(this.baseForTimeEstimation, temporalAmount);

        if (duration.isNegative()) {
            builder.append('-');
            duration = duration.negated();
        }

        List<String> keys = new ArrayList<>(this.units.keySet());
        Collections.reverse(keys);

        for (String key : keys) {
            ChronoUnit chronoUnit = this.units.get(key);
            Long part = UNIT_TO_NANO.get(chronoUnit);

            if (part == null) {
                throw new IllegalArgumentException("Unsupported unit " + chronoUnit);
            }

            BigInteger currentCount = this.durationToNano(duration).divide(BigInteger.valueOf(part));
            BigInteger maxCount = BigInteger.valueOf(PART_TIME_UNITS.get(chronoUnit));
            BigInteger count = currentCount.equals(maxCount) ? BigInteger.ONE : currentCount.mod(maxCount);

            if (count.equals(BigInteger.ZERO)) {
                continue;
            }

            builder.append(count).append(key);
            duration = duration.minusNanos(count.longValue() * part);
        }

        return builder.toString();
    }

    protected abstract Duration toDuration(LocalDateTimeProvider baseForTimeEstimation, T temporalAmount);

    /**
     * A provider for {@link LocalDateTime}.
     * It is used to calculate the estimated time.
     * Depending on the basis, calculations of {@link TemporalAmountParser} may be different.
     * For example, if the basis is {@link LocalDateTimeProvider#now}, then the estimated time is calculated from the current time.
     */
    public interface LocalDateTimeProvider {
        LocalDateTime get();

        static LocalDateTimeProvider now() {
            return LocalDateTime::now;
        }

        static LocalDateTimeProvider startOfToday() {
            return of(LocalDate.now());
        }

        static LocalDateTimeProvider of(LocalDateTime localDateTime) {
            return () -> localDateTime;
        }

        static LocalDateTimeProvider of(LocalDate localDate) {
            return localDate::atStartOfDay;
        }

    }

    BigInteger durationToNano(Duration duration) {
        return BigInteger.valueOf(duration.getSeconds())
            .multiply(BigInteger.valueOf(1_000_000_000))
            .add(BigInteger.valueOf(duration.getNano()));
    }

}

