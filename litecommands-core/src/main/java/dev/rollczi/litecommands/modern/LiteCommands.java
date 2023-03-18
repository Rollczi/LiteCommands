package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandManager;

public interface LiteCommands<SENDER> {

    CommandManager<SENDER, ?> getCommandManager();

    void unregister();

}
