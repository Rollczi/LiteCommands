package dev.rollczi.litecommands.velocity.context;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

/**
 * Built-in context provider that provides server information instance if the sender is a player and is connected to a server.
 * Otherwise, it returns an error message. You can modify the message using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, "Not connected to any server!");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, (invocation, player) -&gt; "Hello, " + player.getUsername() + "! You are not connected to any server!");
 * </pre></blockquote>
 */
public class ServerInfoContextProvider implements ContextProvider<CommandSource, ServerInfo> {

    private final MessageRegistry<CommandSource> messageRegistry;

    public ServerInfoContextProvider(MessageRegistry<CommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ServerInfo> provide(Invocation<CommandSource> invocation) {
        if (invocation.sender() instanceof Player) {
            Player sender = (Player) invocation.sender();

            return sender.getCurrentServer()
                .map(serverConnection -> ContextResult.ok(() -> serverConnection.getServer().getServerInfo()))
                .orElseGet(() -> ContextResult.error(messageRegistry.get(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, invocation, sender)));
        }

        return ContextResult.error(messageRegistry.get(LiteVelocityMessages.PLAYER_ONLY, invocation));
    }

}
