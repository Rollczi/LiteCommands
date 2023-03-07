package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.modern.util.IterableMutableArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestionResult {

    private final List<Suggestion> suggestions;
    private final int suggestionLevel;

    private SuggestionResult(List<Suggestion> suggestions, int suggestionLevel) {
        this.suggestions = suggestions;
        this.suggestionLevel = suggestionLevel;
    }

    public static SuggestionResult of(String... suggestions) {
        return of(new IterableMutableArray<>(suggestions));
    }

    public static SuggestionResult of(Iterable<String> suggestions) {
        List<Suggestion> parsedSuggestions = new ArrayList<>();

        int level = -1;
        for (String suggestion : suggestions) {
            String[] rawSuggestion = suggestion.split(" ");

            if (level == -1) {
                level = rawSuggestion.length;
            }

            if (level != rawSuggestion.length) {
                throw new IllegalArgumentException("Suggestion level is not equal to previous suggestions. Expected: " + level + ", got: " + rawSuggestion.length);
            }

            parsedSuggestions.add(Suggestion.multilevel(suggestion));
        }

        return new SuggestionResult(parsedSuggestions, level);
    }

    public static SuggestionResult from(Suggestion... suggestions) {
        return from(new IterableMutableArray<>(suggestions));
    }

    public static SuggestionResult from(Iterable<Suggestion> suggestions) {
        List<Suggestion> checkedSuggestions = new ArrayList<>();

        int level = -1;

        for (Suggestion suggestion : suggestions) {
            if (level == -1) {
                level = suggestion.lengthMultilevel();
            }
            else if (level != suggestion.lengthMultilevel()) {
                throw new IllegalArgumentException();
            }

            checkedSuggestions.add(suggestion);
        }

        return new SuggestionResult(checkedSuggestions, level);
    }

    public void with(Suggestion suggestion) {
        if (this.suggestionLevel != suggestion.lengthMultilevel()) {
            throw new IllegalArgumentException();
        }

        this.suggestions.add(suggestion);
    }

    public List<Suggestion> getSuggestions() {
        return Collections.unmodifiableList(this.suggestions);
    }

}
