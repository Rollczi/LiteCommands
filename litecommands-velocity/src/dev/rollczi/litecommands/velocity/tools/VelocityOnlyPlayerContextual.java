package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

/**
 * @deprecated Change the build-in {@link LiteVelocityMessages#PLAYER_ONLY} message instead.
 * You can modify all messages using {@link LiteCommandsBuilder#message(MessageKey, Object)}.
 * <br>
 * For example:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.PLAYER_ONLY, "Your custom message here");
 * </pre></blockquote>
 * or with the lambda function:
 * <blockquote><pre>
 * builder.message(LiteVelocityMessages.PLAYER_ONLY, (invocation, __) -&gt; "Your custom message here");
 * </pre></blockquote>
 */
@Deprecated
public class VelocityOnlyPlayerContextual<MESSAGE> implements ContextProvider<CommandSource, Player> {

    private final MESSAGE onlyPlayerMessage;

    public VelocityOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandSource> invocation) {
        if (invocation.sender() instanceof Player) {
            return ContextResult.ok(() -> (Player) invocation.sender());
        }

        return ContextResult.error(onlyPlayerMessage);
    }

}
