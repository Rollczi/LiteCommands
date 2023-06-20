package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;

public class LiteCommandsBase<SENDER> implements LiteCommands<SENDER> {

    protected final CommandManager<SENDER> commandManager;

    public LiteCommandsBase(CommandManager<SENDER> commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public CommandManager<SENDER> getCommandManager() {
        return commandManager;
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
