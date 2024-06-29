package dev.rollczi.litecommands.input.raw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface RawInput {
    static RawInput of(List<String> rawArguments) {
        return new RawInputImpl(new ArrayList<>(rawArguments));
    }

    static RawInput of(String... rawArguments) {
        return new RawInputImpl(new ArrayList<>(Arrays.asList(rawArguments)));
    }

    boolean hasNext();

    String next();

    List<String> next(int count);

    List<String> nextAll();

    default int nextInt() {
        return Integer.parseInt(next());
    }

    default double nextDouble() {
        return Double.parseDouble(next());
    }

    default float nextFloat() {
        return Float.parseFloat(next());
    }

    default long nextLong() {
        return Long.parseLong(next());
    }

    default short nextShort() {
        return Short.parseShort(next());
    }

    default byte nextByte() {
        return Byte.parseByte(next());
    }

    default boolean nextBoolean() {
        return Boolean.parseBoolean(next());
    }

    default char nextChar() {
        return next().charAt(0);
    }

    String seeNext();

    List<String> seeNext(int count);

    List<String> seeAll();

    int size();

    int consumedCount();

}
