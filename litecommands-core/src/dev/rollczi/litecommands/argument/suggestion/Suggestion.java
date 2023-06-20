package dev.rollczi.litecommands.argument.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Suggestion {

    private final String suggestion;
    private final List<String> multiSuggestion;

    private Suggestion(String suggestion, List<String> multiSuggestions) {
        this.suggestion = suggestion;
        this.multiSuggestion = multiSuggestions;
    }

    public String firstLevel() {
        return this.multiSuggestion.get(0);
    }

    public String lastLevel() {
        return this.multiSuggestion.get(this.multiSuggestion.size() - 1);
    }

    public String multilevel() {
        return this.suggestion;
    }

    public List<String> multilevelList() {
        return this.multiSuggestion;
    }

    public boolean isMultilevel() {
        return multiSuggestion.size() > 1;
    }

    public int lengthMultilevel() {
        return this.multiSuggestion.size();
    }

    public Suggestion slashLevel(int level) {
        if (level == 0) {
            return this;
        }

        if (this.multiSuggestion.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return Suggestion.from(this.multiSuggestion.subList(level, this.multiSuggestion.size()));
    }

    public static Suggestion from(List<String> suggestion) {
        if (suggestion.isEmpty()) {
            throw new IllegalArgumentException("Suggestion cannot be empty");
        }

        return new Suggestion(String.join(" ", suggestion), suggestion);
    }

    public static Suggestion of(String suggestion) {
        List<String> collected = new ArrayList<>();
        Collections.addAll(collected, suggestion.split(" "));

        if (suggestion.endsWith(" ") && !collected.get(collected.size() - 1).isEmpty()) {
            collected.add("");
        }

        return new Suggestion(suggestion, collected);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Suggestion)) return false;
        Suggestion that = (Suggestion) o;
        return Objects.equals(suggestion, that.suggestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestion);
    }

    @Override
    public String toString() {
        return this.multilevel();
    }

}
