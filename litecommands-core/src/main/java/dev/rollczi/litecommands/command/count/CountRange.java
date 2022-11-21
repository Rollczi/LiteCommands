package dev.rollczi.litecommands.command.count;

public class CountRange {

    public static final CountRange ZERO = new CountRange(0, 0);
    public static final CountRange ONE = new CountRange(1, 1);

    private final int min;
    private final int max;

    public CountRange(int min, int max) {
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

    public static CountRange range(int min, int max) {
        return new CountRange(min, max);
    }

    public static CountRange onlyRequire(int count) {
        return new CountRange(count, count);
    }

    public static CountRange zero() {
        return ZERO;
    }

    public static CountRange zeroOrMore() {
        return new CountRange(0, Integer.MAX_VALUE);
    }

    public static CountRange moreThan(int min) {
        return new CountRange(min, Integer.MAX_VALUE);
    }

    public static CountRange lessThan(int max) {
        return new CountRange(0, max);
    }

}
