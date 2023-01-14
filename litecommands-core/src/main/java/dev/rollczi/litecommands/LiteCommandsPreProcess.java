package dev.rollczi.litecommands;

import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.platform.RegistryPlatform;

public interface LiteCommandsPreProcess<SENDER> {

    void process(LiteCommandsBuilder<SENDER> builder, RegistryPlatform<SENDER> platform, ExecuteResultHandler<SENDER> executeResultHandler, Injector<SENDER> injector);

}
