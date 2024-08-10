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

        /**
         * When command is not found
         */
        UNKNOWN_COMMAND,

        /**
         * When input is invalid
         * e.g.
         * <pre>
         * command: /command [int]
         * input: /command text
         * </pre>
         */
        INVALID_ARGUMENT,

        /**
         * When input is valid but not enough
         * e.g.
         * <pre>
         * command: /command [text] [x y z]
         * input: /command text
         * </pre>
         */
        MISSING_ARGUMENT,

        /**
         * When input is valid but not enough (part of argument is missing)
         * e.g.
         * <pre>
         * command: /command [text] [x y z]
         * input: /command text 10 20
         * </pre>
         */
        MISSING_PART_OF_ARGUMENT,

        /**
         * When input is valid but too much
         * e.g.
         * <pre>
         * command: /command [text] [x y z]
         * input: /command text 10 20 30 40
         * </pre>
         */
        TOO_MANY_ARGUMENTS
    }

    @Override
    public String toString() {
        return "InvalidUsage{" +
            "cause=" + cause +
            ", lastRoute=" + lastRoute +
            ", schematic=" + schematic +
            '}';
    }
}
