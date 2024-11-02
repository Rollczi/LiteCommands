package dev.rollczi.litecommands.suggestion;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SuggestionResultTooltipCollector<T> implements Collector<T, SuggestionResult, SuggestionResult> {

    private final Function<T, String> suggestionProvider;
    private final Function<T, String> tooltipProvider;

    public SuggestionResultTooltipCollector(Function<T, String> suggestionProvider, Function<T, String> tooltipProvider) {
        this.suggestionProvider = suggestionProvider;
        this.tooltipProvider = tooltipProvider;
    }

    @Override
    public Supplier<SuggestionResult> supplier() {
        return SuggestionResult::of;
    }

    @Override
    public BiConsumer<SuggestionResult, T> accumulator() {
        return (suggestionResult, value) -> suggestionResult.add(Suggestion.of(suggestionProvider.apply(value), tooltipProvider.apply(value)));
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
