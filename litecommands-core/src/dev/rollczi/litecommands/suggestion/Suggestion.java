package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.shared.Preconditions;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.ApiStatus;

public class Suggestion {

    static final String DEFAULT_TOOLTIP = "";

    private final String suggestion;
    private final String tooltip;
    private final List<String> multiSuggestion;

    private Suggestion(String suggestion, String tooltip, List<String> multiSuggestions) {
        Preconditions.notNull(tooltip, "tooltip");
        Preconditions.notNull(suggestion, "suggestion");
        this.suggestion = suggestion;
        this.tooltip = tooltip;
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

    @ApiStatus.Experimental
    public String tooltip() {
        return this.tooltip;
    }

    public Suggestion deleteLeft(int levels) {
        if (levels <= 0) {
            return this;
        }

        if (levels > this.multiSuggestion.size()) {
            throw new IllegalArgumentException("Levels cannot be greater than suggestion size " + levels + " > " + this.multiSuggestion.size());
        }

        return Suggestion.from(this.multiSuggestion.subList(levels, this.multiSuggestion.size()), this.tooltip);
    }

    public Suggestion deleteRight(int levels) {
        if (levels == 0) {
            return this;
        }

        if (levels > this.multiSuggestion.size()) {
            throw new IllegalArgumentException("Levels cannot be greater than suggestion size " + levels + " > " + this.multiSuggestion.size());
        }

        return Suggestion.from(this.multiSuggestion.subList(0, this.multiSuggestion.size() - levels), this.tooltip);
    }

    public Suggestion appendLeft(String... left) {
        return Suggestion.of(String.join(" ", left) + " " + this.suggestion, tooltip);
    }

    public Suggestion appendLeft(Iterable<String> left) {
        List<String> list = Stream.concat(
            StreamSupport.stream(left.spliterator(), false),
            this.multiSuggestion.stream()
        ).collect(Collectors.toList());


        return new Suggestion(String.join(" ", list), this.tooltip, list);
    }

    public Suggestion appendRight(String... right) {
        return Suggestion.of(this.suggestion + " " + String.join(" ", right), this.tooltip);
    }

    public Suggestion appendRight(Iterable<String> right) {
        return Suggestion.of(this.suggestion + " " + String.join(" ", right), this.tooltip);
    }

    @Deprecated
    public Suggestion slashLevel(int level) {
        return this.deleteLeft(level);
    }

    public Suggestion appendLevel(String levelPart) {
        List<String> newSuggestion = new ArrayList<>(this.multiSuggestion);
        newSuggestion.add(levelPart);

        return Suggestion.from(newSuggestion, this.tooltip);
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public static Suggestion from(List<String> suggestion, String tooltip) {
        return new Suggestion(String.join(" ", suggestion), tooltip, new ArrayList<>(suggestion));
    }

    @ApiStatus.Internal
    public static Suggestion from(List<String> suggestion) {
        return new Suggestion(String.join(" ", suggestion), DEFAULT_TOOLTIP, new ArrayList<>(suggestion));
    }

    @ApiStatus.Experimental
    public static Suggestion of(String suggestion, String tooltip) {
        return new Suggestion(suggestion, tooltip, StringUtil.splitBySpace(suggestion));
    }

    public static Suggestion of(String suggestion) {
        return new Suggestion(suggestion, DEFAULT_TOOLTIP, StringUtil.splitBySpace(suggestion));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Suggestion)) return false;
        Suggestion that = (Suggestion) o;
        return Objects.equals(suggestion, that.suggestion) && Objects.equals(tooltip, that.tooltip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestion, tooltip);
    }

    @Override
    public String toString() {
        return this.multilevel();
    }

}
