package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.LiteInvocation;

public final class VelocityUtils {

    private VelocityUtils() {}

    public static LiteInvocation adaptInvocation(String name, SimpleCommand.Invocation invocation) {
        VelocitySender sender = new VelocitySender(invocation.source());
        return new LiteInvocation(name, invocation.alias(), sender, invocation.arguments());
    }

}
