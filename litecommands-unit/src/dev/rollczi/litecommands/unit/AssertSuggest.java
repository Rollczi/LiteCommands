package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        Set<Suggestion> actualSuggestions = suggest.getSuggestions();

        assertThat(actualSuggestions.stream().map(Suggestion::multilevel))
            .containsExactly(suggestions);

        return this;
    }

}
