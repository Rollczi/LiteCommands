package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.route.Route;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import panda.std.Option;

@Route(name = "teleport", aliases = "tp")
@Permission("dev.rollczi.teleport")
public class TeleportCommand {

    @Execute
    public void teleportSelf(Player sender, @Arg Player to) {
        sender.teleport(to.getLocation());
    }

    @Execute
    public void teleportSelfToPosition(Player sender, @Arg Location location, @Arg Option<World> world) {
        location.setWorld(world.orElseGet(sender.getWorld()));
        sender.teleport(location);
    }
/*TODO add @Name
    @Execute
    @Permission("dev.rollczi.teleport.other")
    public void teleportOther(@Arg @Name("target") Player target, @Arg @Name("to") Player to) {
        target.teleport(to.getLocation());
    }*/

}
