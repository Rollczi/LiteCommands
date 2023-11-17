package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.shared.IterableMutableArray;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SuggestionResult {

    private final Set<Suggestion> suggestions;

    private SuggestionResult(Set<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public void addAll(Collection<Suggestion> suggestions) {
       this.suggestions.addAll(suggestions);
    }

    public void add(Suggestion suggestion) {
        this.suggestions.add(suggestion);
    }

    public void addAll(SuggestionResult result) {
        this.suggestions.addAll(result.suggestions);
    }

    public SuggestionResult filterBy(Suggestion suggestion) {
        String multilevel = suggestion.multilevel();
        Set<Suggestion> filtered = this.suggestions.stream()
            .filter(current ->  StringUtil.startsWithIgnoreCase(current.multilevel(), multilevel))
            .map(suggestion1 -> suggestion1.slashLevel(suggestion.lengthMultilevel() - 1))
            .collect(Collectors.toSet());

        return new SuggestionResult(filtered);
    }

    public Set<Suggestion> getSuggestions() {
        return Collections.unmodifiableSet(this.suggestions);
    }

    public List<String> asMultiLevelList() {
        return this.suggestions.stream()
            .map(Suggestion::multilevel)
            .collect(Collectors.toList());
    }

    public static SuggestionResult of(String... suggestions) {
        return of(new IterableMutableArray<>(suggestions));
    }

    public static SuggestionResult empty() {
        return new SuggestionResult(new HashSet<>());
    }

    public static SuggestionResult of(Iterable<String> suggestions) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (String suggestion : suggestions) {
            parsedSuggestions.add(Suggestion.of(suggestion));
        }

        return new SuggestionResult(parsedSuggestions);
    }

    public static SuggestionResult from(Suggestion... suggestions) {
        return from(new IterableMutableArray<>(suggestions));
    }

    public static SuggestionResult from(Iterable<Suggestion> suggestions) {
        Set<Suggestion> checkedSuggestions = new HashSet<>();

        for (Suggestion suggestion : suggestions) {
            checkedSuggestions.add(suggestion);
        }

        return new SuggestionResult(checkedSuggestions);
    }

    public static SuggestionResultCollector collector() {
        return new SuggestionResultCollector();
    }

}
