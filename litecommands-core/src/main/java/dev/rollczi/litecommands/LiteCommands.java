package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;

public interface LiteCommands<SENDER> {

    CommandManager<SENDER, ?> getCommandManager();

    void unregister();

}
