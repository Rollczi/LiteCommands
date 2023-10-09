package dev.rollczi.litecommands.invalidusage;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.schematic.Schematic;

public class InvalidUsage<SENDER> {

    private final Cause cause;
    private final CommandRoute<SENDER> lastRoute;
    private final Schematic schematic;

    public InvalidUsage(Cause cause, CommandRoute<SENDER> lastRoute, Schematic schematic) {
        this.cause = cause;
        this.lastRoute = lastRoute;
        this.schematic = schematic;
    }

    public Cause getCause() {
        return cause;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public CommandRoute<SENDER> getLastCommand() {
        return lastRoute;
    }

    public enum Cause {
        UNKNOWN_COMMAND,
        INVALID_ARGUMENT,
        MISSING_ARGUMENT,
        MISSING_PART_OF_ARGUMENT,
        TOO_MANY_ARGUMENTS
    }

}
