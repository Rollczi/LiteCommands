package dev.rollczi.litecommands.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        return Collections.unmodifiableList(this.multiSuggestion);
    }

    public boolean isMultilevel() {
        return multiSuggestion.size() > 1;
    }

    public int lengthMultilevel() {
        return this.multiSuggestion.size();
    }

    public Suggestion deleteLeft(int levels) {
        if (levels == 0) {
            return this;
        }

        if (levels > this.multiSuggestion.size()) {
            throw new IllegalArgumentException("Levels cannot be greater than suggestion size " + levels + " > " + this.multiSuggestion.size());
        }

        return Suggestion.from(this.multiSuggestion.subList(levels, this.multiSuggestion.size()));
    }

    public Suggestion deleteRight(int levels) {
        if (levels == 0) {
            return this;
        }

        if (levels > this.multiSuggestion.size()) {
            throw new IllegalArgumentException("Levels cannot be greater than suggestion size " + levels + " > " + this.multiSuggestion.size());
        }

        return Suggestion.from(this.multiSuggestion.subList(0, this.multiSuggestion.size() - levels));
    }

    public Suggestion appendLeft(String... left) {
        return Suggestion.of(String.join(" ", left) + " " + this.suggestion);
    }

    public Suggestion appendLeft(Iterable<String> left) {
        List<String> list = Stream.concat(
            StreamSupport.stream(left.spliterator(), false),
            this.multiSuggestion.stream()
        ).collect(Collectors.toList());


        return new Suggestion(String.join(" ", list), list);
    }

    public Suggestion appendRight(String... right) {
        return Suggestion.of(this.suggestion + " " + String.join(" ", right));
    }

    public Suggestion appendRight(Iterable<String> right) {
        return Suggestion.of(this.suggestion + " " + String.join(" ", right));
    }

    @Deprecated
    public Suggestion slashLevel(int level) {
        return this.deleteLeft(level);
    }

    public Suggestion appendLevel(String levelPart) {
        List<String> newSuggestion = new ArrayList<>(this.multiSuggestion);
        newSuggestion.add(levelPart);

        return Suggestion.from(newSuggestion);
    }

    public static Suggestion from(List<String> suggestion) {
        return new Suggestion(String.join(" ", suggestion), new ArrayList<>(suggestion));
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
