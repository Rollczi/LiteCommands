package dev.rollczi.litecommands.suggestion;

import java.util.Objects;

public class Completion {
    private final String suggestion;
    private final String tooltip;

    public Completion(String suggestion, String tooltip) {
        this.suggestion = suggestion;
        this.tooltip = tooltip;
    }

    public String suggestion() {
        return suggestion;
    }

    public String tooltip() {
        return tooltip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Completion that = (Completion) o;
        return Objects.equals(suggestion, that.suggestion) && Objects.equals(tooltip, that.tooltip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestion, tooltip);
    }

    @Override
    public String toString() {
        return "Completion{" +
            "suggestion='" + suggestion + '\'' +
            ", tooltip='" + tooltip + '\'' +
            '}';
    }
}
