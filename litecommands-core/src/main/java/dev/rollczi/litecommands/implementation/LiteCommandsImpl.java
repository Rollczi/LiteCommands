package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.platform.RegistryPlatform;

class LiteCommandsImpl<SENDER> implements LiteCommands<SENDER> {

    private final CommandService<SENDER> commandService;
    private final Injector<SENDER> injector;
    private final Class<SENDER> senderType;

    LiteCommandsImpl(CommandService<SENDER> commandService, Class<SENDER> senderType,Injector<SENDER> injector) {
        this.commandService = commandService;
        this.senderType = senderType;
        this.injector = injector;
    }

    @Override
    public CommandService<SENDER> getCommandService() {
        return this.commandService;
    }

    @Override
    public RegistryPlatform<SENDER> getPlatform() {
        return this.commandService.getPlatform();
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
        return this.commandService.getHandler();
    }

}
