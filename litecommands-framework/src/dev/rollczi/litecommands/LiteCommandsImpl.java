package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    protected final CommandManager<SENDER> commandManager;

    public LiteCommandsImpl(CommandManager<SENDER> commandManager) {
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
