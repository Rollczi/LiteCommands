package dev.rollczi.litecommands.modern.range;

public class Range {

    public static final Range ZERO = new Range(0, 0);
    public static final Range ONE = new Range(1, 1);

    private final int min;
    private final int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isInRange(int count) {
        return count >= min && count <= max;
    }

    public static Range range(int min, int max) {
        return new Range(min, max);
    }

    public static Range of(int count) {
        return new Range(count, count);
    }

    public static Range zero() {
        return ZERO;
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
