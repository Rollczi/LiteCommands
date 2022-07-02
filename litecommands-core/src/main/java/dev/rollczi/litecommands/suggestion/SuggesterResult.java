package dev.rollczi.litecommands.suggestion;

public class SuggesterResult {

    private final UniformSuggestionStack suggestions;
    private final boolean success;

    public SuggesterResult(UniformSuggestionStack suggestions, boolean success) {
        this.suggestions = suggestions;
        this.success = success;
    }

    public UniformSuggestionStack getSuggestions() {
        return suggestions;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

}
