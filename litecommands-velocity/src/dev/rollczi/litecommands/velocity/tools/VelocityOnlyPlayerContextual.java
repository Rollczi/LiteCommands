package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;

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
