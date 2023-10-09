package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Set;

public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        Set<Suggestion> actualSuggestions = suggest.getSuggestions();

        if (suggestions.length != actualSuggestions.size()) {
            throw new AssertionError("Expected " + suggestions.length + " suggestions, but got " + actualSuggestions.size() + " " + actualSuggestions);
        }

        for (String suggestion : suggestions) {
            if (!actualSuggestions.contains(Suggestion.of(suggestion))) {
                throw new AssertionError("Expected suggestion " + suggestion + " but not found in " + actualSuggestions);
            }
        }

        return this;
    }

}
