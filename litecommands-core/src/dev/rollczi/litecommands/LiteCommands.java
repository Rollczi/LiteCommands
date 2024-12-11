package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.platform.Platform;
import org.jetbrains.annotations.ApiStatus;

public interface LiteCommands<SENDER> {

    @Deprecated
    String VERSION = LiteCommandsVariables.VERSION;

    CommandManager<SENDER> getCommandManager();

    Platform<SENDER, ?> getPlatform();

    @ApiStatus.Experimental
    LiteCommandsInternal<SENDER, ?> getInternal();

    void register();

    void unregister();

}
