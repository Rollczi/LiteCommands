package dev.rollczi.litecommands.bungee.context;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.bungee.LiteBungeeMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Built-in context provider that provides a player instance if the sender is a player.
 * Otherwise, it returns an error message. You can modify the message using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteBungeeMessages.PLAYER_ONLY, "Your custom message here");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteBungeeMessages.PLAYER_ONLY, (invocation, __) -> "Your custom message here");
 * </pre></blockquote>
 */
public class PlayerOnlyContextProvider implements ContextProvider<CommandSender, ProxiedPlayer> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public PlayerOnlyContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ProxiedPlayer> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof ProxiedPlayer) {
            return ContextResult.ok(() -> (ProxiedPlayer) invocation.sender());
        }

        return ContextResult.error(messageRegistry.get(LiteBungeeMessages.PLAYER_ONLY, invocation));
    }

}
