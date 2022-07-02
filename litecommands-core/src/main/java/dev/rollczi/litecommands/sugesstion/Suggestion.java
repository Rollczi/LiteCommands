package dev.rollczi.litecommands.sugesstion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public int lengthMultilevel() {
        return this.suggestion.size();
    }

    public Suggestion slashLevel(int level) {
        if (this.suggestion.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return Suggestion.multilevel(this.suggestion.subList(level, this.suggestion.size()));
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

    public static Suggestion of(String suggestion) {
        return new Suggestion(Collections.singletonList(suggestion));
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

}
