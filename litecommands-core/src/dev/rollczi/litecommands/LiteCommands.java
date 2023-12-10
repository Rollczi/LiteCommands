package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;
import org.jetbrains.annotations.ApiStatus;

public interface LiteCommands<SENDER> {

    @Deprecated
    String VERSION = LiteCommandsVariables.VERSION;

    CommandManager<SENDER> getCommandManager();

    @ApiStatus.Experimental
    LiteCommandsInternal<SENDER, ?> getInternal();

    void register();

    void unregister();

}
