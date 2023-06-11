package dev.rollczi.litecommands.suggestion;

public class SuggestionContext {

    private final Suggestion current;

    public SuggestionContext(Suggestion current) {
        this.current = current;
    }

    public SuggestionContext(String single) {
        this.current = Suggestion.of(single);
    }

    public Suggestion getCurrent() {
        return current;
    }

}
