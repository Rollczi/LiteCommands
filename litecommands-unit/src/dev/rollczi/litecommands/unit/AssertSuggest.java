package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        assertThat(getSuggestions().stream().map(Suggestion::multilevel))
            .containsAll(Arrays.asList(suggestions));

        return this;
    }

    public Collection<Suggestion> getSuggestions() {
        return suggest.getSuggestions();
    }
}
