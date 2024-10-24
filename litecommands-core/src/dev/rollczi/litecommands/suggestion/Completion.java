package dev.rollczi.litecommands.suggestion;

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
}
