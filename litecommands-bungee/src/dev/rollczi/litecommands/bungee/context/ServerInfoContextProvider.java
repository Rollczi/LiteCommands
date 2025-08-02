package dev.rollczi.litecommands.bungee.context;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextChainAccessor;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

/**
 * Built-in context provider that provides a server instance if the sender is a player and is connected to a server.
 * Otherwise, it returns an error message. You can modify the message using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteBungeeMessages.NOT_CONNECTED_TO_ANY_SERVER, "Not connected to any server!");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteBungeeMessages.NOT_CONNECTED_TO_ANY_SERVER, (invocation, player) -> "Hello, " + player.getName() + "! You are not connected to any server!");
 * </pre></blockquote>
 */
public class ServerInfoContextProvider implements ContextChainedProvider<CommandSender, ServerInfo> {

    @Override
    public ContextResult<ServerInfo> provide(Invocation<CommandSender> invocation, ContextChainAccessor<CommandSender> accessor) {
        return accessor.provideContext(Server.class, invocation)
            .map(server -> server.getInfo());
    }

}
