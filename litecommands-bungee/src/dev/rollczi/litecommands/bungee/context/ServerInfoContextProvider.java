package dev.rollczi.litecommands.bungee.context;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.bungee.LiteBungeeMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class ServerInfoContextProvider implements ContextProvider<CommandSender, ServerInfo> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public ServerInfoContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ServerInfo> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) invocation.sender();

            if (sender.getServer() == null) {
                return ContextResult.error(messageRegistry.get(LiteBungeeMessages.NOT_CONNECTED_TO_ANY_SERVER, invocation, sender));
            }

            return ContextResult.ok(() -> sender.getServer().getInfo());
        }

        return ContextResult.error(messageRegistry.get(LiteBungeeMessages.PLAYER_ONLY, invocation));
    }

}
