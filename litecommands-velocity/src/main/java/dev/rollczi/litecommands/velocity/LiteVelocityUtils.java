package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import panda.utilities.StringUtils;

public final class LiteVelocityUtils {

    private LiteVelocityUtils() {

    }

    public static LiteInvocation adaptInvocation(String name, SimpleCommand.Invocation invocation, LiteSenderCreator<CommandSource> creator) {
        return new LiteInvocation(name, invocation.alias(),  creator.create(invocation.source()), invocation.arguments());
    }

    public static LiteInvocation adaptSuggestInvocation(String name, SimpleCommand.Invocation invocation, LiteSenderCreator<CommandSource> creator) {
        String[] arguments = invocation.arguments();

        if (arguments.length == 0) {
            arguments = new String[] { StringUtils.EMPTY };
        }

        return new LiteInvocation(name, invocation.alias(),  creator.create(invocation.source()), arguments);
    }



}
