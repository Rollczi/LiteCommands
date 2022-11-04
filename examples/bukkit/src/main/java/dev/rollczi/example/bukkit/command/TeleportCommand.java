package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import panda.std.Option;

@Route(name = "teleport", aliases = "tp")
@Permission("dev.rollczi.teleport")
public class TeleportCommand {

    @Execute(required = 1)
    public void teleportSelf(Player sender, @Arg Player to) {
        sender.teleport(to.getLocation());
    }

    @Execute(min = 3, max = 4)
    public void teleportSelfToPosition(Player sender, @Arg Location location, @Opt Option<World> world) {
        location.setWorld(world.orElseGet(sender.getWorld()));
        sender.teleport(location);
    }

    @Execute(required = 2)
    @Permission("dev.rollczi.teleport.other")
    public void teleportOther(@Arg @Name("target") Player target, @Arg @Name("to") Player to) {
        target.teleport(to.getLocation());
    }

}
