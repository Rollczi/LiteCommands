package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;
import org.jetbrains.annotations.ApiStatus;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    protected final CommandManager<SENDER> commandManager;
    protected final LiteCommandsInternal<SENDER, ?> internal;

    public LiteCommandsImpl(CommandManager<SENDER> commandManager, LiteCommandsInternal<SENDER, ?> internal) {
        this.commandManager = commandManager;
        this.internal = internal;
    }

    @Override
    public CommandManager<SENDER> getCommandManager() {
        return commandManager;
    }

    @Override
    @ApiStatus.Experimental
    public LiteCommandsInternal<SENDER, ?> getInternal() {
        return internal;
    }

    @Override
    public void register() {
        commandManager.registerAll();
    }

    @Override
    public void unregister() {
        commandManager.unregisterAll();
    }

}
