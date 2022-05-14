package dev.rollczi.example.bukkit;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import panda.std.Option;

@Section(route = "teleport", aliases = "tp")
@Permission("dev.rollczi.teleport")
public class TeleportCommand {

    @Execute(min = 3, max = 4)
    public void to(Player sender, @Arg Location location, @Opt Option<World> world) {
        location.setWorld(world.orElseGet(sender.getWorld()));
        sender.teleport(location);
    }

    @Execute(required = 1)
    public void toPlayer(Player sender, @Arg Player to) {
        sender.teleport(to.getLocation());
    }

    @Execute(required = 2)
    @Permission("dev.rollczi.teleport.other")
    public void targetToPlayer(@Arg Player target, @Arg Player to) {
        target.teleport(to.getLocation());
    }

}
