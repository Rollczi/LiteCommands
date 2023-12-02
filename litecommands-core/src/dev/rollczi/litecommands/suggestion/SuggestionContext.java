package dev.rollczi.litecommands.suggestion;

public class SuggestionContext {

    private final Suggestion current;
    private int consumed = 0;

    public SuggestionContext(Suggestion current) {
        this.current = current;
        this.consumed = current.lengthMultilevel();
    }

    public SuggestionContext(String single) {
        this.current = Suggestion.of(single);
    }

    public Suggestion getCurrent() {
        return current;
    }

    public int getConsumed() {
        return consumed;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }

}
