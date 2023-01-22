package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.platform.RegistryPlatform;

public interface LiteCommandsPostProcess<SENDER> {

    void process(LiteCommandsBuilder<SENDER> builder, RegistryPlatform<SENDER> platform, Injector<SENDER> injector, ExecuteResultHandler<SENDER> executeResultHandler, CommandService<SENDER> commandService);

}
