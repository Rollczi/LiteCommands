package dev.rollczi.litecommands.shared;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class TemporalAmountParser<T extends TemporalAmount> {

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

    public static final TemporalAmountParser<Duration> TIME_UNITS = TemporalAmountParser.createDuration()
        .withUnit("ms", ChronoUnit.MILLIS)
        .withUnit("s", ChronoUnit.SECONDS)
        .withUnit("m", ChronoUnit.MINUTES)
        .withUnit("h", ChronoUnit.HOURS);

    public static final TemporalAmountParser<Period> DATE_UNITS = TemporalAmountParser.createPeriod()
        .withUnit("d", ChronoUnit.DAYS)
        .withUnit("w", ChronoUnit.WEEKS)
        .withUnit("mo", ChronoUnit.MONTHS)
        .withUnit("y", ChronoUnit.YEARS)
        .withDurationEstimation(true);

    public static final TemporalAmountParser<Duration> DATE_TIME_UNITS = TemporalAmountParser.createDuration()
        .withUnit("ns", ChronoUnit.NANOS)
        .withUnit("us", ChronoUnit.MICROS)
        .withUnit("ms", ChronoUnit.MILLIS)
        .withUnit("s", ChronoUnit.SECONDS)
        .withUnit("m", ChronoUnit.MINUTES)
        .withUnit("h", ChronoUnit.HOURS)
        .withUnit("d", ChronoUnit.DAYS)
        .withUnit("w", ChronoUnit.WEEKS)
        .withUnit("mo", ChronoUnit.MONTHS)
        .withUnit("y", ChronoUnit.YEARS)
        .withDurationEstimation(true);

    private final Map<String, ChronoUnit> units = new LinkedHashMap<>();
    private final boolean timeEstimationSupported;
    private final Function<Duration, T> temporalAmountFactory;
    private final Function<T, Duration> temporalAmountToDuration;

    private TemporalAmountParser(Function<Duration, T> temporalAmountFactory, Function<T, Duration> temporalAmountToDuration) {
        this.temporalAmountFactory = temporalAmountFactory;
        this.temporalAmountToDuration = temporalAmountToDuration;
        this.timeEstimationSupported = false;
    }

    private TemporalAmountParser(Map<String, ChronoUnit> units, boolean timeEstimationSupported, Function<Duration, T> temporalAmountFactory, Function<T, Duration> temporalAmountToDuration) {
        this.temporalAmountFactory = temporalAmountFactory;
        this.temporalAmountToDuration = temporalAmountToDuration;
        this.units.putAll(units);
        this.timeEstimationSupported = timeEstimationSupported;
    }

    public TemporalAmountParser<T> withUnit(String symbol, ChronoUnit chronoUnit) {
        if (units.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol " + symbol + " is already used");
        }

        if (this.onCharacters(symbol, c -> !Character.isLetter(c))) {
            throw new IllegalArgumentException("Symbol " + symbol + " contains non-letter characters");
        }

        Map<String, ChronoUnit> newUnits = new LinkedHashMap<>(units);
        newUnits.put(symbol, chronoUnit);
        return new TemporalAmountParser<>(newUnits, timeEstimationSupported, temporalAmountFactory, temporalAmountToDuration);
    }

    public TemporalAmountParser<T> withDurationEstimation(boolean enabled) {
        return new TemporalAmountParser<>(units, enabled, temporalAmountFactory, temporalAmountToDuration);
    }

    private boolean onCharacters(String content, Predicate<Character> predicate) {
        for (int i = 0; i < content.length(); i++) {
            if (!predicate.test(content.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public T parse(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }

        Duration total = Duration.ZERO;

        StringBuilder number = new StringBuilder();
        StringBuilder unit = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c) || (c == '-' && number.length() == 0)) {
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
                Duration duration = this.parseDuration(number.toString(), unit.toString());

                total = total.plus(duration);

                number.setLength(0);
                unit.setLength(0);
            }
        }

        return temporalAmountFactory.apply(total);
    }

    private Duration parseDuration(String number, String unit) {
        if (number.isEmpty()) {
            throw new IllegalArgumentException("Missing number before unit " + unit);
        }

        if (unit.isEmpty()) {
            throw new IllegalArgumentException("Missing unit after number " + number);
        }

        ChronoUnit chronoUnit = units.get(unit);

        if (chronoUnit == null) {
            throw new IllegalArgumentException("Unknown unit " + unit);
        }

        try {
            long count = Long.parseLong(number);

            if (chronoUnit.isDurationEstimated()) {
                if (!timeEstimationSupported) {
                    throw new IllegalArgumentException("Time estimation is not supported");
                }

                LocalDateTime localDate = LocalDateTime.now();
                LocalDateTime estimatedDate = localDate.plus(count, chronoUnit);

                return Duration.between(localDate, estimatedDate);
            }

            return Duration.of(count, chronoUnit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number " + number);
        }
    }

    public String format(T amount) {
        StringBuilder builder = new StringBuilder();
        Duration duration = temporalAmountToDuration.apply(amount);

        List<String> keys = new ArrayList<>(units.keySet());
        Collections.reverse(keys);

        for (String key : keys) {
            ChronoUnit chronoUnit = units.get(key);
            Long part = UNIT_TO_NANO.get(chronoUnit);

            if (part == null) {
                throw new IllegalArgumentException("Unknown unit " + chronoUnit);
            }

            long allCount = duration.toNanos() / part;
            long count = allCount % PART_TIME_UNITS.get(chronoUnit);

            if (count == 0) {
                continue;
            }

            builder.append(count).append(key);
            duration = duration.minusNanos(count * part);
        }

        return builder.toString();
    }

    public static TemporalAmountParser<Duration> createDuration() {
        return new TemporalAmountParser<>(duration -> duration, duration -> duration);
    }

    public static TemporalAmountParser<Period> createPeriod() {
        return new TemporalAmountParser<>(duration -> Period.ofDays((int) duration.toDays()), period -> Duration.ofDays(period.getDays()));
    }

}
