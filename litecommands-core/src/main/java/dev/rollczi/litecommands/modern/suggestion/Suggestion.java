package dev.rollczi.litecommands.modern.suggestion;

import java.util.*;

public class Suggestion {

    private final List<String> multiSuggestion;

    private Suggestion(List<String> multiSuggestion) {
        this.multiSuggestion = multiSuggestion;
    }

    public String multilevel() {
        return String.join(" ", this.multiSuggestion);
    }

    public List<String> multilevelList() {
        return this.multiSuggestion;
    }

    public String single() {
        return this.multiSuggestion.get(0);
    }

    public boolean isMultilevel() {
        return multiSuggestion.size() > 1;
    }

    public int lengthMultilevel() {
        return this.multiSuggestion.size();
    }

    public Suggestion slashLevel(int level) {
        if (this.multiSuggestion.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return Suggestion.multilevel(this.multiSuggestion.subList(level, this.multiSuggestion.size()));
    }

    public static Suggestion multilevel(List<String> suggestion) {
        if (suggestion.isEmpty()) {
            throw new IllegalArgumentException("Suggestion cannot be empty");
        }

        return new Suggestion(new ArrayList<>(suggestion));
    }

    public static Suggestion multilevel(String... suggestion) {
        return Suggestion.multilevel(Arrays.asList(suggestion));
    }

    public static List<Suggestion> of(String... suggestions) {
        return of(Arrays.asList(suggestions));
    }

    public static List<Suggestion> of(Iterable<String> suggestions) {
        List<Suggestion> complete = new ArrayList<>();

        for (String suggestion : suggestions) {
            complete.add(Suggestion.of(suggestion));
        }

        return complete;
    }

    public static Suggestion of(String suggestion) {
        return new Suggestion(Collections.singletonList(suggestion));
    }

    public static Suggestion empty() {
        return new Suggestion(Collections.emptyList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Suggestion)) {
            return false;
        }

        Suggestion that = (Suggestion) obj;
        return this.multilevel().equals(that.multilevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.multilevel());
    }

    @Override
    public String toString() {
        return this.multilevel();
    }

}
