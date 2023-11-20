package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.logger.LiteLogger;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    protected final CommandManager<SENDER> commandManager;
    protected final LiteLogger logger;

    public LiteCommandsImpl(CommandManager<SENDER> commandManager, LiteLogger logger) {
        this.commandManager = commandManager;
        this.logger = logger;
    }

    @Override
    public CommandManager<SENDER> getCommandManager() {
        return commandManager;
    }

    @Override
    public LiteLogger getLogger() {
        return logger;
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
