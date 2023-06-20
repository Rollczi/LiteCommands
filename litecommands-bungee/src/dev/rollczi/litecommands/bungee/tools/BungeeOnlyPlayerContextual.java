package dev.rollczi.litecommands.bungee.tools;

import dev.rollczi.litecommands.context.LegacyContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

public class BungeeOnlyPlayerContextual<MESSAGE> implements LegacyContextProvider<CommandSender, ProxiedPlayer> {

    private final Supplier<MESSAGE> onlyPlayerMessage;

    public BungeeOnlyPlayerContextual(Supplier<MESSAGE> onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    public BungeeOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this(() -> onlyPlayerMessage);
    }

    @Override
    public Result<ProxiedPlayer, Object> provideLegacy(Invocation<CommandSender> invocation) {
        return Option.of(invocation.sender())
            .is(ProxiedPlayer.class)
            .toResult(onlyPlayerMessage.get());
    }

}
