package dev.rollczi.litecommands.bukkit.adventure.tools;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class KyoriBukkitOnlyPlayerContextual implements Contextual<CommandSender, Player> {

    private final Component onlyPlayerMessage;

    public KyoriBukkitOnlyPlayerContextual(Component onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        return Option.of(sender).is(Player.class).toResult(onlyPlayerMessage);
    }

}
