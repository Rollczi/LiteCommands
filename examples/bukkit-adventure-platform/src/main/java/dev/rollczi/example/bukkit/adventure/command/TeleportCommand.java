package dev.rollczi.example.bukkit.adventure.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Command(name = "teleport", aliases = "tp")
@Permission("dev.rollczi.teleport")
public class TeleportCommand {

    @Execute
    public void teleportSelf(@Context Player sender, @Context Audience senderAudience, @Arg Player to) {
        sender.teleport(to.getLocation());
    }

    @Execute
    public void teleportSelfToPosition(@Context Player sender, @Arg Location location, @Arg Optional<World> world) {
        location.setWorld(world.orElse(sender.getWorld()));
        sender.teleport(location);
    }

    @Execute
    @Permission("dev.rollczi.teleport.other")
    public void teleportOther(@Arg("target") Player target, @Arg("to") Player to) {
        target.teleport(to.getLocation());
    }

}
