package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.modern.util.IterableMutableArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SuggestionResult {

    public final static Collector<String, SuggestionResult, SuggestionResult> COLLECTOR = new Collector<String, SuggestionResult, SuggestionResult>() {
        @Override
        public Supplier<SuggestionResult> supplier() {
            return SuggestionResult::of;
        }

        @Override
        public BiConsumer<SuggestionResult, String> accumulator() {
            return (suggestionResult, raw) -> suggestionResult.with(Suggestion.of(raw));
        }

        @Override
        public BinaryOperator<SuggestionResult> combiner() {
            return (suggestionResult, suggestionResult2) -> {
                SuggestionResult finalResult = SuggestionResult.from(suggestionResult.suggestions);

                finalResult.with(suggestionResult2.suggestions);

                return finalResult;
            };
        }

        @Override
        public Function<SuggestionResult, SuggestionResult> finisher() {
            return suggestionResult -> suggestionResult;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    };

    private final List<Suggestion> suggestions;
    private int suggestionLevel;

    private SuggestionResult(List<Suggestion> suggestions, int suggestionLevel) {
        this.suggestions = suggestions;
        this.suggestionLevel = suggestionLevel;
    }

    private void with(List<Suggestion> suggestions) {
        for (Suggestion suggestion : suggestions) {
            with(suggestion);
        }
    }

    public void with(Suggestion suggestion) {
        if (this.suggestionLevel == -1) {
            this.suggestions.add(suggestion);
            this.suggestionLevel = suggestion.lengthMultilevel();
        }

        if (this.suggestionLevel != suggestion.lengthMultilevel()) {
            throw new IllegalArgumentException();
        }

        this.suggestions.add(suggestion);
    }

    public List<Suggestion> getSuggestions() {
        return Collections.unmodifiableList(this.suggestions);
    }

    public List<String> asMultiLevelList() {
        return this.suggestions.stream()
            .map(suggestion -> suggestion.multilevel())
            .collect(Collectors.toList());
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

}
