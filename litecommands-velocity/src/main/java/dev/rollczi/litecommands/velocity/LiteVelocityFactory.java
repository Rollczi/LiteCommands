package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.implementation.LiteFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import panda.std.Result;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {
    }

    public static LiteCommandsBuilder<CommandSource> builder(ProxyServer proxy) {
        return builder(proxy, false);
    }

    public static LiteCommandsBuilder<CommandSource> builder(ProxyServer proxy, boolean nativePermissions) {
        return LiteFactory.builder(CommandSource.class)
                .typeBind(ProxyServer.class, () -> proxy)

                .argument(Joiner.class, Component.class, new KyoriComponentJoinerArgument())
                .argument(Component.class, new KyoriComponentArgument())
                .argument(Component.class, "color", new KyoriColoredComponentArgument())

                .contextualBind(Audience.class, new KyoriAudienceContextual())
                .contextualBind(CommandSource.class, (commandSource, invocation) -> Result.ok(commandSource))

                .resultHandler(String.class, new StringHandler())
                .resultHandler(Component.class, new KyoriComponentHandler())

                .platform(new LiteVelocityRegistryPlatform(proxy, nativePermissions));
    }

}
