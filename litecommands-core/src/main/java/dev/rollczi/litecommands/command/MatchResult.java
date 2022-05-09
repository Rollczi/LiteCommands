package dev.rollczi.litecommands.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MatchResult {

    private final boolean matched;
    private final List<Object> results;
    private final int consumed;

    private MatchResult(boolean matched, List<Object> results, int consumed) {
        this.matched = matched;
        this.results = results;
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

    public int getConsumed() {
        return consumed;
    }

    public static <T> MatchResult matched(List<T> results, int consumed) {
        return new MatchResult(true, new ArrayList<>(results), consumed);
    }

    public static MatchResult matched(Object result, int consumed) {
        return new MatchResult(true, Collections.singletonList(result), consumed);
    }

    public static MatchResult matchedSingle(Object result) {
        return new MatchResult(true, Collections.singletonList(result), 1);
    }

    public static MatchResult notMatched() {
        return new MatchResult(false, Collections.emptyList(), 0);
    }

}
