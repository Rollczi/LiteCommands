package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.platform.RegistryPlatform;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    private final CommandService<SENDER> commandService;
    private final RegistryPlatform<SENDER> platform;
    private final Injector<SENDER> injector;
    private final Class<SENDER> senderType;
    private final ExecuteResultHandler<SENDER> executeResultHandler;

    LiteCommandsImpl(Class<SENDER> senderType, RegistryPlatform<SENDER> platform, ExecuteResultHandler<SENDER> handler, Injector<SENDER> injector) {
        this.senderType = senderType;
        this.injector = injector;
        this.commandService = new CommandService<>(platform, handler);
        this.platform = platform;
        this.executeResultHandler = handler;
    }

    @Override
    public CommandService<SENDER> getCommandService() {
        return this.commandService;
    }

    @Override
    public RegistryPlatform<SENDER> getPlatform() {
        return this.platform;
    }

    @Override
    public Injector<SENDER> getInjector() {
        return injector;
    }

    @Override
    public Class<SENDER> getSenderType() {
        return this.senderType;
    }

    @Override
    public ExecuteResultHandler<SENDER> getExecuteResultHandler() {
        return executeResultHandler;
    }

}
