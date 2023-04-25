package dev.rollczi.litecommands.invalid;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.schematic.Schematic;

public class InvalidUsage<SENDER> {

    private Cause cause;
    private CommandRoute<SENDER> route;
    private Schematic schematic;

    public enum Cause {

        UNKNOWN_COMMAND,
        TOO_MANY_ARGUMENTS,
        TOO_FEW_ARGUMENTS,
        INVALID_ARGUMENT,
        MISSING_ARGUMENT,

    }

    public Cause getCause() {
        return cause;
    }

    public Schematic getSchematic() {
        return schematic;
    }

}
