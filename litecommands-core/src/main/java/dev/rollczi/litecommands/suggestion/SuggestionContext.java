package dev.rollczi.litecommands.suggestion;

import java.util.List;

public class SuggestionContext {

    private final List<String> currentArgumentSuggestions;
    private final String currentSuggestion;

    public SuggestionContext(List<String> currentArgumentSuggestions, String currentSuggestion) {
        this.currentArgumentSuggestions = currentArgumentSuggestions;
        this.currentSuggestion = currentSuggestion;
    }

    public String getCurrent() {
        return currentSuggestion;
    }

    public boolean isCurrentStarting() {
        return this.currentSuggestion.isEmpty();
    }

}
