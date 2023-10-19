package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SuppressWarnings({"UnusedReturnValue", "Convert2MethodRef"})
public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        assertThat(suggest.getSuggestions().stream().map(suggestion -> suggestion.multilevel()))
            .containsAll(Arrays.asList(suggestions));
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

    public @Unmodifiable Collection<Suggestion> getSuggestions() {
        return Collections.unmodifiableCollection(suggest.getSuggestions());
    }
}
