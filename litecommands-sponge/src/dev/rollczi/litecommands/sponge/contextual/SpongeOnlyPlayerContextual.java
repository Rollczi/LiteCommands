package dev.rollczi.litecommands.sponge.contextual;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeOnlyPlayerContextual<MESSAGE> implements ContextProvider<CommandCause, Player> {

    private final MESSAGE onlyPlayerMessage;

    public SpongeOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandCause> invocation) {
        if (invocation.sender().root() instanceof Player) {
            return ContextResult.ok(() -> (Player) invocation.sender().root());
        }

        return ContextResult.error(onlyPlayerMessage);
    }

}
