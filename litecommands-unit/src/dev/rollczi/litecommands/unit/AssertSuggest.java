package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SuppressWarnings({"UnusedReturnValue", "Convert2MethodRef"})
public class AssertSuggest {

    private final SuggestionResult suggest;

    public AssertSuggest(SuggestionResult suggest) {
        this.suggest = suggest;
    }

    public AssertSuggest assertSuggestAndFlush(Collection<String> suggestions) {
        return assertSuggestAndFlush(suggestions.toArray(new String[0]));
    }

    public AssertSuggest assertSuggestAndFlush(String... suggestions) {
        assertThat(suggest.getSuggestions()
            .stream()
            .map(Suggestion::multilevel)
            .filter(suggestion -> !suggestion.isEmpty())
        ).contains(suggestions);

        for (String suggestion : suggestions) {
            suggest.remove(Suggestion.of(suggestion));
        }

        return this;
    }

    public AssertSuggest assertSuggest() {
        return assertSuggest(new String[0]);
    }

    public AssertSuggest assertSuggest(String... suggestions) {
        return assertSuggest(Arrays.asList(suggestions));
    }

    /**
     * Simulates the suggester and asserts the result
     * Can be used for testing other things than suggesters (e.g. array resolvers)
     */
    public AssertSuggest assertAsSuggester(Suggester<TestSender, ?> suggester, String input) {
        return assertAsSuggester(suggester, Function.identity(), input);
    }

    /**
     * Simulates the suggester and asserts the result, with a mapper
     * Can be used for testing other things than suggesters (e.g. array resolvers)
     */
    public AssertSuggest assertAsSuggester(Suggester<TestSender, ?> suggester, Function<String, String> mapper, String input) {
        return assertAsSuggester(suggester, null, mapper, input);
    }

    public AssertSuggest assertAsSuggester(Suggester<TestSender, ?> suggester, Argument<?> argument, Function<String, String> mapper, String input) {
        Suggestion suggestion = Suggestion.of(input);
        SuggestionInput<?> suggestionInput = SuggestionInput.raw(suggestion.multilevelList().toArray(new String[0]));
        Invocation<TestSender> invocation = new Invocation<>(new TestSender(), TestPlatformSender.permittedAll(), "-", "-", suggestionInput);
        SuggestionResult suggestionResult = suggester.suggest(invocation, (Argument) argument, new SuggestionContext(suggestion));

        SuggestionResult result = suggestionResult
            .filterBy(suggestion);

        SuggestionResult mapped = SuggestionResult.empty();

        for (Suggestion resultSuggestion : result.getSuggestions()) {
            mapped.add(Suggestion.of(mapper.apply(resultSuggestion.multilevel())));
        }

        return assertSuggest(mapped);
    }

    public AssertSuggest assertSuggest(SuggestionResult suggestions) {
        return assertSuggest(suggestions.getSuggestions().toArray(new Suggestion[0]));
    }

    public AssertSuggest assertSuggest(Collection<String> suggestions) {
        assertThat(suggest.getSuggestions()
            .stream()
            .map(Suggestion::multilevel)
            .filter(suggestion -> !suggestion.isEmpty())
        ).containsExactlyInAnyOrderElementsOf(suggestions);
        return this;
    }

    public AssertSuggest assertSuggest(Suggestion... suggestions) {
        assertThat(suggest.getSuggestions()
            .stream()
            .map(Suggestion::tooltip)
            .filter(tooltip -> tooltip != null && !tooltip.isEmpty())
        ).containsExactlyInAnyOrderElementsOf(Arrays.stream(suggestions).map(Suggestion::tooltip).filter(tooltip -> tooltip != null && !tooltip.isEmpty()).collect(Collectors.toList()));
        assertThat(suggest.getSuggestions()
            .stream()
            .map(Suggestion::multilevel)
            .filter(suggestion -> !suggestion.isEmpty())
        ).containsExactlyInAnyOrderElementsOf(Arrays.stream(suggestions).map(Suggestion::multilevel).filter(suggestion -> !suggestion.isEmpty()).collect(Collectors.toList()));
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

    public AssertSuggest mapIf(boolean contains, @NotNull Function<String, String> mapper) {
        if (contains) {
            Set<Suggestion> set = suggest.getSuggestions().stream()
                .map(suggestion -> suggestion.multilevel())
                .map(mapper)
                .map(suggestion -> Suggestion.of(suggestion))
                .collect(Collectors.toSet());

            suggest.clear();

            for (Suggestion suggestion : set) {
                suggest.add(suggestion);
            }
        }

        return this;
    }

    public AssertSuggest assertEmpty() {
        assertThat(suggest.getSuggestions()).isEmpty();
        return this;
    }

}
