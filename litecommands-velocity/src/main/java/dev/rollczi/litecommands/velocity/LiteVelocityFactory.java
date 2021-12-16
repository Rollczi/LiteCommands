package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteFactory;
import dev.rollczi.litecommands.inject.basic.OriginalSenderBind;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {

    }

    public static LiteCommandsBuilder builder(ProxyServer proxy) {
        return LiteFactory.builder()
                .bind(ProxyServer.class, proxy)
                .bind(CommandSource.class, new OriginalSenderBind())
                .platform(new LiteVelocityPlatformManager(proxy));
    }

}
