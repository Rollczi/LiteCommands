package dev.rollczi.litecommands.argument.displayname;

public class ArgumentDisplayName {

    private final int priority;
    private final String displayName;

    private ArgumentDisplayName(int priority, String displayName) {
        this.priority = priority;
        this.displayName = displayName;
    }

    public int getPriority() {
        return priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ArgumentDisplayName lowest(String displayName) {
        return new ArgumentDisplayName(-100, displayName);
    }

    public static ArgumentDisplayName low(String displayName) {
        return new ArgumentDisplayName(-50, displayName);
    }

    public static ArgumentDisplayName normal(String displayName) {
        return new ArgumentDisplayName(0, displayName);
    }

    public static ArgumentDisplayName high(String displayName) {
        return new ArgumentDisplayName(50, displayName);
    }

    public static ArgumentDisplayName highest(String displayName) {
        return new ArgumentDisplayName(100, displayName);
    }

    public static ArgumentDisplayName other(int priority, String displayName) {
        return new ArgumentDisplayName(priority, displayName);
    }

}
