package dev.rollczi.litecommands.command;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchResult {

    private final boolean matched;
    private final List<Object> results;
    private final @Nullable Object noMatchedResult;
    private final int consumed;

    private MatchResult(boolean matched, List<Object> results, @Nullable Object noMatchedResult, int consumed) {
        this.matched = matched;
        this.results = results;
        this.noMatchedResult = noMatchedResult;
        this.consumed = consumed;
    }

    public boolean isMatched() {
        return matched;
    }

    public boolean isNotMatched() {
        return !matched;
    }

    public List<Object> getResults() {
        return results;
    }

    public Optional<Object> getNoMatchedResult() {
        return Optional.ofNullable(noMatchedResult);
    }

    public int getConsumed() {
        return consumed;
    }

    public static MatchResult matched(List<Object> results, int consumed) {
        return new MatchResult(true, new ArrayList<>(results), null, consumed);
    }

    public static MatchResult matched(Object result, int consumed) {
        return new MatchResult(true, Collections.singletonList(result), null, consumed);
    }

    public static MatchResult matchedSingle(Object result) {
        return new MatchResult(true, Collections.singletonList(result), null, 1);
    }

    public static MatchResult notMatched() {
        return new MatchResult(false, Collections.emptyList(), null, 0);
    }

    public static MatchResult notMatched(Object noMatchedResult) {
        return new MatchResult(false, Collections.emptyList(), noMatchedResult, 0);
    }

}
