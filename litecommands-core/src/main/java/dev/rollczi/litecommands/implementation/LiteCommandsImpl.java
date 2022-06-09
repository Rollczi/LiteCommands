package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.platform.RegistryPlatform;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    private final Class<SENDER> senderType;
    private final CommandService<SENDER> commandService;
    private final RegistryPlatform<SENDER> platform;

    LiteCommandsImpl(Class<SENDER> senderType, RegistryPlatform<SENDER> platform, ExecuteResultHandler<SENDER> handler) {
        this.senderType = senderType;
        this.commandService = new CommandService<>(platform, handler);
        this.platform = platform;
    }

    @Override
    public Class<SENDER> getSenderType() {
        return this.senderType;
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
