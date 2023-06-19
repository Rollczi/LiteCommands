package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;

public interface SchematicFormat {

    String format(CommandRoute<?> commandRoute);

    String format(CommandExecutor<?, ?> executor);

    static SchematicFormat simple() {
        return new SchematicFormatImpl();
    }


}
