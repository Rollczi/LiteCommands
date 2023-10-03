package dev.rollczi.litecommands.bungee.tools;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.function.Supplier;

public class BungeeOnlyPlayerContextual<MESSAGE> implements ContextProvider<CommandSender, ProxiedPlayer> {

    private final Supplier<MESSAGE> onlyPlayerMessage;

    public BungeeOnlyPlayerContextual(Supplier<MESSAGE> onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    public BungeeOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this(() -> onlyPlayerMessage);
    }

    @Override
    public ContextResult<ProxiedPlayer> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof ProxiedPlayer) {
            return ContextResult.ok(() -> (ProxiedPlayer) invocation.sender());
        }

        return ContextResult.error(onlyPlayerMessage.get());
    }

}
