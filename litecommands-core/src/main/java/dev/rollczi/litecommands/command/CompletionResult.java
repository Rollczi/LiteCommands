package dev.rollczi.litecommands.command;

import panda.std.stream.PandaStream;
import panda.utilities.text.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompletionResult {

    private final List<Completion> completions;

    private CompletionResult(List<Completion> completions) {
        this.completions = completions;
    }

    public List<Completion> getCompletions() {
        return completions;
    }

    public List<String> completionsWithSpace() {
        return completions.stream()
                .map(completion -> Joiner.on(" ").join(completion).toString())
                .collect(Collectors.toList());
    }

    public List<String> completionsWithFirst() {
        return PandaStream.of(completions)
                .map(Completion::asStringFirstPart)
                .collect(Collectors.toList());
    }

    public CompletionResult with(Completion... completions) {
        return this.with(Arrays.asList(completions));
    }

    public CompletionResult with(Iterable<Completion> completions) {
        ArrayList<Completion> list = new ArrayList<>(this.completions);

        for (Completion completion : completions) {
            list.add(completion);
        }

        return new CompletionResult(list);
    }

    public static CompletionResult empty() {
        return new CompletionResult(Collections.emptyList());
    }

    public static CompletionResult of(Iterable<Completion> completions) {
        return CompletionResult.empty().with(completions);
    }

}
