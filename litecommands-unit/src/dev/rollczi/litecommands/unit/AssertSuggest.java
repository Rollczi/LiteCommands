package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        assertThat(getSuggestions()).containsAll(Arrays.asList(suggestions));
        return this;
    }

    public AssertSuggest assertNotEmpty() {
        assertThat(suggest.getSuggestions()).isNotEmpty();
        return this;
    }

    public AssertSuggest assertCorrect(Consumer<Suggestion> suggestionAction) {
        for (Suggestion suggestion : suggest.getSuggestions()) {
            try {
                suggestionAction.accept(suggestion);
            } catch (AssertionError e) {
                throw new AssertionError("Suggestion '" + suggestion + "' was not valid", e);
            }
        }
        return this;
    }

    public Collection<String> getSuggestions() {
        return suggest.getSuggestions().stream().map(Suggestion::multilevel).collect(Collectors.toList());
    }

}
