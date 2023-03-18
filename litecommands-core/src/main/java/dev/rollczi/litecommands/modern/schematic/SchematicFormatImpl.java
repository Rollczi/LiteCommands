package dev.rollczi.litecommands.modern.schematic;

import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;

class SchematicFormatImpl implements SchematicFormat {

    @Override
    public String format(CommandRoute<?> commandRoute) {
        return commandRoute.getName();
    }

    @Override
    public String format(CommandExecutor<?> executor) {
        for (PreparedArgument<?, ?> argument : executor.getArguments()) {
            // TODO
        }

        return "null";
    }

}
