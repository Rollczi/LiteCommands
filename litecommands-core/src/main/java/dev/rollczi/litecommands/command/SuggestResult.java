package dev.rollczi.litecommands.command;

import panda.std.stream.PandaStream;
import panda.utilities.text.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestResult {

    private final List<Suggestion> suggestions;

    private SuggestResult(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public List<String> suggestionsWithSpace() {
        return suggestions.stream()
                .map(Suggestion::asStringWithSpaces)
                .collect(Collectors.toList());
    }

    public List<String> suggestionWithFirst() {
        return PandaStream.of(suggestions)
                .map(Suggestion::asStringFirstPart)
                .collect(Collectors.toList());
    }

    public SuggestResult with(Suggestion... suggestions) {
        return this.with(Arrays.asList(suggestions));
    }

    public SuggestResult with(Iterable<Suggestion> suggestions) {
        ArrayList<Suggestion> list = new ArrayList<>(this.suggestions);

        for (Suggestion suggestion : suggestions) {
            list.add(suggestion);
        }

        return new SuggestResult(list);
    }

    public static SuggestResult empty() {
        return new SuggestResult(Collections.emptyList());
    }

    public static SuggestResult of(Iterable<Suggestion> suggestions) {
        return SuggestResult.empty().with(suggestions);
    }

}
