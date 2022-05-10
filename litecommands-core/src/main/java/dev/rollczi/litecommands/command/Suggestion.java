package dev.rollczi.litecommands.command;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Suggestion {

    private final List<String> suggestion;

    private Suggestion(List<String> suggestion) {
        this.suggestion = suggestion;
    }

    public String asStringWithSpaces() {
        return String.join(" ", this.suggestion);
    }

    public String asStringFirstPart() {
        return this.suggestion.get(0);
    }

    @Nullable
    public String asStringPart(int index) {
        return this.suggestion.get(index);
    }

    public List<String> asList() {
        return new ArrayList<>(this.suggestion);
    }

    public static Suggestion multiSuggestion(List<String> suggestion) {
        if (suggestion.isEmpty()) {
            throw new IllegalArgumentException("Suggestion cannot be empty");
        }

        return new Suggestion(new ArrayList<>(suggestion));
    }

    public static Suggestion multiSuggestion(String... suggestion) {
        return Suggestion.multiSuggestion(Arrays.asList(suggestion));
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
