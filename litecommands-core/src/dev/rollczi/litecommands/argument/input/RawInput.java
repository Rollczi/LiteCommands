package dev.rollczi.litecommands.argument.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RawInput {

    private final List<String> rawArgumentsToConsume = new ArrayList<>();
    private int consumed = 0;

    private RawInput(Collection<String> rawArgumentsToConsume) {
        this.rawArgumentsToConsume.addAll(rawArgumentsToConsume);
    }

    public String consumeNext() {
        consumed++;
        return rawArgumentsToConsume.remove(0);
    }

    public List<String> consumeAll() {
        List<String> consumedArguments = new ArrayList<>(rawArgumentsToConsume);
        this.rawArgumentsToConsume.clear();
        this.consumed += consumedArguments.size();

        return consumedArguments;
    }

    public String spyNext() {
        return rawArgumentsToConsume.get(0);
    }

    public List<String> spyAll() {
        return Collections.unmodifiableList(rawArgumentsToConsume);
    }

    public boolean hasNext() {
        return !rawArgumentsToConsume.isEmpty();
    }

    public boolean hasNoNext() {
        return rawArgumentsToConsume.isEmpty();
    }

    public int size() {
        return rawArgumentsToConsume.size();
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
