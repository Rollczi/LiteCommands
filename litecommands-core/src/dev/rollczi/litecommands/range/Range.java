package dev.rollczi.litecommands.range;

import org.jetbrains.annotations.ApiStatus;

public class Range {

    public static final Range ZERO_POINT = new Range(0, 0);
    public static final Range ONE = new Range(1, 1);

    private final int min;
    private final int max;

    public Range(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max cannot be less than min");
        }

        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isPoint() {
        return min == max;
    }

    public boolean isPointZero() {
        return min == 0 && max == 0;
    }

    public boolean isInRange(int count) {
        return count >= min && count <= max;
    }

    @ApiStatus.Experimental
    public boolean isInRangeOrAbove(int count) {
        return count >= min;
    }

    @ApiStatus.Experimental
    public boolean isInRangeOrBelow(int count) {
        return count <= max;
    }

    public boolean isOutOfRange(int count) {
        return !isInRange(count);
    }

    public boolean isAboveRange(int count) {
        return count > max;
    }

    public boolean isBelowRange(int count) {
        return count < min;
    }

    public static Range range(int min, int max) {
        return new Range(min, max);
    }

    public static Range of(int count) {
        return new Range(count, count);
    }

    public static Range zero() {
        return ZERO_POINT;
    }

    public static Range zeroOrMore() {
        return new Range(0, Integer.MAX_VALUE);
    }

    public static Range moreThan(int min) {
        return new Range(min, Integer.MAX_VALUE);
    }

    public static Range lessThan(int max) {
        return new Range(0, max);
    }

}
