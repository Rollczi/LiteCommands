package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;

public interface LiteCommands<SENDER> {

    @Deprecated
    String VERSION = LiteCommandsVariables.VERSION;

    CommandManager<SENDER> getCommandManager();

    void register();

    void unregister();

}
