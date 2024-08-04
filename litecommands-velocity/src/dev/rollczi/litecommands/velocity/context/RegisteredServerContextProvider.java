package dev.rollczi.litecommands.velocity.context;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

/**
 * Built-in context provider that provides a server instance if the sender is a player and is connected to a server.
 * Otherwise, it returns an error message. You can modify the message using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, "Not connected to any server!");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, (invocation, player) -> "Hello, " + player.getUsername() + "! You are not connected to any server!");
 * </pre></blockquote>
 */
public class RegisteredServerContextProvider implements ContextProvider<CommandSource, RegisteredServer> {

    private final MessageRegistry<CommandSource> messageRegistry;

    public RegisteredServerContextProvider(MessageRegistry<CommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<RegisteredServer> provide(Invocation<CommandSource> invocation) {
        if (invocation.sender() instanceof Player) {
            Player sender = (Player) invocation.sender();

            return sender.getCurrentServer()
                .map(serverConnection -> ContextResult.ok(() -> serverConnection.getServer()))
                .orElseGet(() -> ContextResult.error(messageRegistry.getInvoked(LiteVelocityMessages.NOT_CONNECTED_TO_ANY_SERVER, invocation, sender)));
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteVelocityMessages.PLAYER_ONLY, invocation));
    }

}
