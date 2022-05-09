package dev.rollczi.litecommands.command;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Completion {

    private final List<String> completion;

    private Completion(List<String> completion) {
        this.completion = completion;
    }

    public String asStringWithSpaces() {
        return String.join(" ", this.completion);
    }

    public String asStringFirstPart() {
        return this.completion.get(0);
    }

    @Nullable
    public String asStringPart(int index) {
        return this.completion.get(index);
    }

    public List<String> asList() {
        return new ArrayList<>(this.completion);
    }

    public static Completion multiCompletion(List<String> completion) {
        if (completion.isEmpty()) {
            throw new IllegalArgumentException("Completion cannot be empty");
        }

        return new Completion(new ArrayList<>(completion));
    }

    public static Completion multiCompletion(String... completion) {
        return Completion.multiCompletion(Arrays.asList(completion));
    }

    public static Completion of(String completion) {
        return new Completion(Collections.singletonList(completion));
    }

    public static List<Completion> of(Iterable<String> completions) {
        List<Completion> complete = new ArrayList<>();

        for (String completion : completions) {
            complete.add(Completion.of(completion));
        }

        return complete;
    }

}
