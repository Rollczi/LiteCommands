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

    public boolean hasNext() {
        return !rawArgumentsToConsume.isEmpty();
    }

    public String next() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        consumed++;
        return rawArgumentsToConsume.remove(0);
    }

    public List<String> next(int count) {
        if (count > rawArgumentsToConsume.size()) {
            throw new IllegalArgumentException("Cannot consume next " + count + " arguments, only " + rawArgumentsToConsume.size() + " left");
        }

        consumed += count;
        List<String> consumedArguments = new ArrayList<>(rawArgumentsToConsume).subList(0, count);
        rawArgumentsToConsume.removeAll(consumedArguments);
        return consumedArguments;
    }

    public List<String> nextAll() {
        return next(rawArgumentsToConsume.size());
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

    public String seeNext() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        return rawArgumentsToConsume.get(0);
    }

    public List<String> seeNext(int count) {
        if (count > rawArgumentsToConsume.size()) {
            throw new IllegalArgumentException("Cannot see next " + count + " arguments, only " + rawArgumentsToConsume.size() + " left");
        }

        return rawArgumentsToConsume.subList(0, count);
    }

    public List<String> seeAll() {
        return seeNext(rawArgumentsToConsume.size());
    }

    public int size() {
        return rawArgumentsToConsume.size();
    }

    int consumedCount() {
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
