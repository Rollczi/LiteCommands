package dev.rollczi.litecommands.suggestion.generator;

import org.jetbrains.annotations.Nullable;

public class SuggestionGeneratorResult {

    private final @Nullable String skipSuggestion;
    private final boolean failure;

    private SuggestionGeneratorResult(@Nullable String skipSuggestion, boolean failure) {
        this.skipSuggestion = skipSuggestion;
        this.failure = failure;
    }

    public boolean isFailure() {
        return failure;
    }

    public boolean isSkip() {
        return skipSuggestion != null;
    }

    public @Nullable String getSkipSuggestion() {
        return skipSuggestion;
    }

    public boolean isGenerate() {
        return !isFailure() && !isSkip();
    }

    public static SuggestionGeneratorResult generate() {
        return new SuggestionGeneratorResult(null, false);
    }

    public static SuggestionGeneratorResult skip(String suggestion) {
        return new SuggestionGeneratorResult(suggestion, false);
    }

    public static SuggestionGeneratorResult failure() {
        return new SuggestionGeneratorResult(null, true);
    }

}
