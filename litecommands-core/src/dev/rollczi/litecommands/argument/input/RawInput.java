package dev.rollczi.litecommands.argument.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class RawInput {

    private final List<String> rawArgumentsToConsume = new ArrayList<>();
    private int consumed = 0;

    private RawInput(Collection<String> rawArgumentsToConsume) {
        this.rawArgumentsToConsume.addAll(rawArgumentsToConsume);
    }

    public String next() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        consumed++;
        return rawArgumentsToConsume.remove(0);
    }

    public List<String> nextAll() {
        List<String> consumedArguments = new ArrayList<>(rawArgumentsToConsume);
        this.rawArgumentsToConsume.clear();
        this.consumed += consumedArguments.size();

        return consumedArguments;
    }

    public String seeNext() {
        return rawArgumentsToConsume.get(0);
    }

    public List<String> seeAll() {
        return Collections.unmodifiableList(rawArgumentsToConsume);
    }

    public boolean hasNext() {
        return !rawArgumentsToConsume.isEmpty();
    }

    int consumedCount() {
        return this.consumed;
    }

    static RawInput of(Collection<String> rawArguments) {
        return new RawInput(rawArguments);
    }

    public static RawInput of(String... rawArguments) {
        return new RawInput(Arrays.asList(rawArguments));
    }

    static RawInput empty() {
        return new RawInput(new ArrayList<>());
    }

}
