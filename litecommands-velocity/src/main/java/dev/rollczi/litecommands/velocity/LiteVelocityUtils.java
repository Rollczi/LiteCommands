package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.LiteInvocation;

public final class LiteVelocityUtils {

    private LiteVelocityUtils() {}

    public static LiteInvocation adaptInvocation(String name, SimpleCommand.Invocation invocation) {
        LiteVelocitySender sender = new LiteVelocitySender(invocation.source());
        return new LiteInvocation(name, invocation.alias(), sender, invocation.arguments());
    }

}
