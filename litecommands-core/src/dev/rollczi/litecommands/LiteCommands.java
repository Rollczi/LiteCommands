package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.logger.LiteLogger;

public interface LiteCommands<SENDER> {

    @Deprecated
    String VERSION = LiteCommandsVariables.VERSION;

    CommandManager<SENDER> getCommandManager();

    LiteLogger getLogger();

    void register();

    void unregister();

}
