package dev.rollczi.litecommands.command.sugesstion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Suggestion {

    private final List<String> suggestion;

    private Suggestion(List<String> suggestion) {
        this.suggestion = suggestion;
    }

    public String multilevel() {
        return String.join(" ", this.suggestion);
    }

    public String single() {
        return this.suggestion.get(0);
    }

    public boolean isMultilevel() {
        return suggestion.size() > 1;
    }

    public Suggestion slashLevel(int level) {
        if (this.suggestion.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return Suggestion.multilevelSuggestion(this.suggestion.subList(level, this.suggestion.size()));
    }

    public static Suggestion multilevelSuggestion(List<String> suggestion) {
        if (suggestion.isEmpty()) {
            throw new IllegalArgumentException("Suggestion cannot be empty");
        }

        return new Suggestion(new ArrayList<>(suggestion));
    }

    public static Suggestion multilevelSuggestion(String... suggestion) {
        return Suggestion.multilevelSuggestion(Arrays.asList(suggestion));
    }

    public static Suggestion of(String suggestion) {
        return new Suggestion(Collections.singletonList(suggestion));
    }

    public static List<Suggestion> of(Iterable<String> suggestions) {
        List<Suggestion> complete = new ArrayList<>();

        for (String suggestion : suggestions) {
            complete.add(Suggestion.of(suggestion));
        }

        return complete;
    }

}
