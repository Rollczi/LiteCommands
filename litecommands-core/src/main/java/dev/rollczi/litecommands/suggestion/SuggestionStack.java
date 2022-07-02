package dev.rollczi.litecommands.suggestion;

import panda.std.stream.PandaStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SuggestionStack {

    protected final LinkedHashSet<Suggestion> suggestions;

    protected SuggestionStack(List<Suggestion> suggestions) {
        this.suggestions = new LinkedHashSet<>(suggestions);
    }

    public Set<Suggestion> suggestions() {
        return Collections.unmodifiableSet(suggestions);
    }

    public List<String> multilevelSuggestions() {
        return suggestions.stream()
                .map(Suggestion::multilevel)
                .collect(Collectors.toList());
    }

    public List<String> singleSuggestion() {
        return PandaStream.of(suggestions)
                .map(Suggestion::single)
                .collect(Collectors.toList());
    }

    public SuggestionStack with(Suggestion... suggestions) {
        return this.with(Arrays.asList(suggestions));
    }

    public SuggestionStack with(Iterable<Suggestion> suggestions) {
        ArrayList<Suggestion> list = new ArrayList<>(this.suggestions);

        for (Suggestion suggestion : suggestions) {
            list.add(suggestion);
        }

        return of(list);
    }

    public static SuggestionStack empty() {
        return new SuggestionStack(Collections.emptyList());
    }

    public static SuggestionStack of(Collection<Suggestion> suggestions) {
        return new SuggestionStack(new ArrayList<>(suggestions));
    }

    public boolean isEmpty() {
        return suggestions.isEmpty();
    }

}
