package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.shared.IterableMutableArray;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;

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

    @ApiStatus.Internal
    public void remove(Suggestion suggestion) {
        this.suggestions.remove(suggestion);
    }

    @ApiStatus.Internal
    public void clear() {
        this.suggestions.clear();
    }

    public void addAll(SuggestionResult result) {
        this.suggestions.addAll(result.suggestions);
    }

    public SuggestionResult filterBy(Suggestion suggestion) {
        String multilevel = suggestion.multilevel();
        int levels = suggestion.lengthMultilevel();
        Set<Suggestion> filtered = this.suggestions.stream()
            .filter(current ->  StringUtil.startsWithIgnoreCase(current.multilevel(), multilevel))
            .map(suggestion1 -> suggestion1.deleteLeft(levels - 1))
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

    public SuggestionResult appendLeft(String... suggestions) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (Suggestion suggestion : this.suggestions) {
            parsedSuggestions.add(suggestion.appendLeft(suggestions));
        }

        return new SuggestionResult(parsedSuggestions);
    }

    public SuggestionResult appendLeft(
        Iterable<String> suggestions
    ) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (Suggestion suggestion : this.suggestions) {
            parsedSuggestions.add(suggestion.appendLeft(suggestions));
        }

        return new SuggestionResult(parsedSuggestions);
    }

    /**
     * Appends the given part directly to the left of the suggestion. (without any space)
     */
    @ApiStatus.Experimental
    public SuggestionResult appendLeftDirectly(String partToAppend) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (Suggestion suggestion : this.suggestions) {
            parsedSuggestions.add(Suggestion.of(partToAppend + suggestion.multilevel()));
        }

        return new SuggestionResult(parsedSuggestions);
    }

    public SuggestionResult appendRight(
        Iterable<String> suggestions
    ) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (Suggestion suggestion : this.suggestions) {
            parsedSuggestions.add(suggestion.appendRight(suggestions));
        }

        return new SuggestionResult(parsedSuggestions);
    }

    public SuggestionResult appendRight(
        String... suggestions
    ) {
        Set<Suggestion> parsedSuggestions = new HashSet<>();

        for (Suggestion suggestion : this.suggestions) {
            parsedSuggestions.add(suggestion.appendRight(suggestions));
        }

        return new SuggestionResult(parsedSuggestions);
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
