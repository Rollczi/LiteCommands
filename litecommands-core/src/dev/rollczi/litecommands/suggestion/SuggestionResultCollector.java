package dev.rollczi.litecommands.suggestion;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SuggestionResultCollector implements Collector<String, SuggestionResult, SuggestionResult> {
    private final boolean copyTooltip;

    public SuggestionResultCollector(boolean copyTooltip) {
        this.copyTooltip = copyTooltip;
    }

    @Override
    public Supplier<SuggestionResult> supplier() {
        return SuggestionResult::of;
    }

    @Override
    public BiConsumer<SuggestionResult, String> accumulator() {
        return (suggestionResult, raw) -> {
            if (copyTooltip) {
                suggestionResult.add(Suggestion.of(raw, raw));
            } else {
                suggestionResult.add(Suggestion.of(raw));
            }
        };
    }

    @Override
    public BinaryOperator<SuggestionResult> combiner() {
        return (first, second) -> {
            SuggestionResult finalResult = SuggestionResult.from(first.getSuggestions());

            finalResult.addAll(second.getSuggestions());

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

}
