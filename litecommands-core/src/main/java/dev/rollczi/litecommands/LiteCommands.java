package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.platform.RegistryPlatform;

public interface LiteCommands<SENDER> {

    Class<SENDER> getSenderType();

    CommandService<SENDER> getCommandService();

    RegistryPlatform<SENDER> getPlatform();

}
