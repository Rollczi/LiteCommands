package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteFactory;
import dev.rollczi.litecommands.bind.basic.OriginalSenderBind;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {

    }

    public static LiteCommandsBuilder<CommandSource, LiteVelocityPlatformManager> builder(ProxyServer proxy) {
        return LiteFactory.<CommandSource, LiteVelocityPlatformManager>builder()
                .typeBind(ProxyServer.class, proxy)
                .typeBind(CommandSource.class, new OriginalSenderBind())
                .platform(new LiteVelocityPlatformManager(proxy));
    }

}
