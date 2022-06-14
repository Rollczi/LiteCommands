package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.platform.RegistryPlatform;

public interface LiteCommands<SENDER> {

    CommandService<SENDER> getCommandService();

    RegistryPlatform<SENDER> getPlatform();

    Injector<SENDER> getInjector();

    Class<SENDER> getSenderType();

    ExecuteResultHandler<SENDER> getExecuteResultHandler();
}
