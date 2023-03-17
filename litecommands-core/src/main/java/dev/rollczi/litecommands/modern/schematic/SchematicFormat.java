package dev.rollczi.litecommands.modern.schematic;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;

public interface SchematicFormat {

    String format(CommandRoute<?> commandRoute);

    String format(CommandExecutor<?> executor);

    static SchematicFormat simple() {
        return new SchematicFormatImpl();
    }


}
