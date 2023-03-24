package dev.rollczi.litecommands.invalid;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.schematic.Schematic;

public class InvalidUsage<SENDER> {

    private Cause cause;
    private CommandRoute<SENDER> route;
    private Schematic schematic;

    public enum Cause {

        UNKNOWN_COMMAND,
        MISSING_ARGUMENT,
        INVALID_ARGUMENT,

    }

    public Cause getCause() {
        return cause;
    }

    public Schematic getSchematic() {
        return schematic;
    }
}
