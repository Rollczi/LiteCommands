package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class MinestomOnlyPlayerContext<MESSAGE> implements ContextProvider<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public MinestomOnlyPlayerContext(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof Player player) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error(onlyPlayerMessage);
    }

}
