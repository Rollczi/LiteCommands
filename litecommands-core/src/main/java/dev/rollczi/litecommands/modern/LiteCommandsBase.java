package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandManager;

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
    public void unregister() {
        commandManager.unregisterAll();
    }

}
