package dev.rollczi.litecommands.command;

public final class InvalidUsage {

    public static final InvalidUsage INSTANCE = new InvalidUsage();

    public InvalidUsage() {
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InvalidUsage;
    }

}
