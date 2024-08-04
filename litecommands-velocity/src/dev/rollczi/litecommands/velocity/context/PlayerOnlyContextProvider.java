package dev.rollczi.litecommands.velocity.context;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

/**
 * Built-in context provider that provides a player instance if the sender is a player.
 * Otherwise, it returns an error message. You can modify the message using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.PLAYER_ONLY, "Your custom message here");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.PLAYER_ONLY, (invocation, __) -> "Your custom message here");
 * </pre></blockquote>
 */
public class PlayerOnlyContextProvider implements ContextProvider<CommandSource, Player> {

    private final MessageRegistry<CommandSource> messageRegistry;

    public PlayerOnlyContextProvider(MessageRegistry<CommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandSource> invocation) {
        if (invocation.sender() instanceof Player) {
            return ContextResult.ok(() -> (Player) invocation.sender());
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteVelocityMessages.PLAYER_ONLY, invocation));
    }

}
