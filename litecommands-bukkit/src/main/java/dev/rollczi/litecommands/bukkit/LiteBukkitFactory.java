package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix) {
        LiteBukkitRegistryPlatform registryPlatform = new LiteBukkitRegistryPlatform(server, fallbackPrefix);

        return LiteFactory.builder(CommandSender.class)
            .typeBind(Server.class, () -> server)

            .resultHandler(String.class, new StringHandler())

            .platform(registryPlatform)
            .afterRegister((builder, platform, injector, executeResultHandler, commandService) -> registryPlatform.setExecuteResultHandler(executeResultHandler));
    }

}
