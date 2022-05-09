package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.platform.RegistryPlatform;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    private final CommandService<SENDER> commandService;
    private final RegistryPlatform<SENDER> platform;

    LiteCommandsImpl(RegistryPlatform<SENDER> platform, ExecuteResultHandler<SENDER> handler) {
        this.commandService = new CommandService<>(platform, handler);
        this.platform = platform;
    }

    @Override
    public CommandService<SENDER> getCommandService() {
        return this.commandService;
    }

    @Override
    public RegistryPlatform<SENDER> getPlatform() {
        return this.platform;
    }

}
