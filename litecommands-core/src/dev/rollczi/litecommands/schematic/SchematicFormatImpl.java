package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;

class SchematicFormatImpl implements SchematicFormat {

    @Override
    public String format(CommandRoute<?> commandRoute) {
        return commandRoute.getName();
    }

    @Override
    public String format(CommandExecutor<?, ?> executor) {
//        for (PreparedArgument<?, ?> argument : executor.getArguments()) {
//            // TODO
//        }

        return "null";
    }

}
