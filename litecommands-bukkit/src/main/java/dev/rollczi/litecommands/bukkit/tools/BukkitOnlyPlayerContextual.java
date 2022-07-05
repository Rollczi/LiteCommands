package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class BukkitOnlyPlayerContextual implements Contextual<CommandSender, Player> {

    private final String onlyPlayer;

    public BukkitOnlyPlayerContextual(String onlyPlayerMessage) {
        this.onlyPlayer = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        return Option.of(sender).is(Player.class).toResult(onlyPlayer);
    }

}
