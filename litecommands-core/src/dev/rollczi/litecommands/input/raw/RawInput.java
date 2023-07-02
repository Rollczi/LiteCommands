package dev.rollczi.litecommands.input.raw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class RawInput {

    private final List<String> rawArgumentsToConsume;
    private int consumed = 0;

    private RawInput(List<String> rawArgumentsToConsume) {
        this.rawArgumentsToConsume = rawArgumentsToConsume;
    }

    public String next() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        consumed++;
        return rawArgumentsToConsume.remove(0);
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public float nextFloat() {
        return Float.parseFloat(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public short nextShort() {
        return Short.parseShort(next());
    }

    public byte nextByte() {
        return Byte.parseByte(next());
    }

    public boolean nextBoolean() {
        return Boolean.parseBoolean(next());
    }

    public char nextChar() {
        return next().charAt(0);
    }

    @Deprecated
    public List<String> nextAll() {
        List<String> consumedArguments = new ArrayList<>(rawArgumentsToConsume);
        this.rawArgumentsToConsume.clear();
        this.consumed += consumedArguments.size();

        return consumedArguments;
    }

    public String seeNext() {
        return rawArgumentsToConsume.get(0);
    }

    @Deprecated
    public List<String> seeAll() {
        return Collections.unmodifiableList(rawArgumentsToConsume);
    }

    public boolean hasNext() {
        return !rawArgumentsToConsume.isEmpty();
    }

    public int consumedCount() {
        return this.consumed;
    }

    public static RawInput of(List<String> rawArguments) {
        return new RawInput(new ArrayList<>(rawArguments));
    }

    public static RawInput of(String... rawArguments) {
        return new RawInput(new ArrayList<>(Arrays.asList(rawArguments)));
    }

    static RawInput empty() {
        return new RawInput(Collections.emptyList());
    }

}
