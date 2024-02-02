package dev.rollczi.example.sponge.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

@Command(name = "teleport", aliases = "tp")
// @Permission("dev.rollczi.teleport") | we make it available by default
public class TeleportCommand {

    @Execute
    public void teleportSelf(@Context Player sender, @Arg ServerPlayer to) {
        ServerPlayer serverSender = (ServerPlayer) sender;

        serverSender.setLocation(to.serverLocation());
        sender.sendMessage(Component.text("You were teleported to player " + to.name()));
    }

    @Execute
    @Permission("dev.rollczi.teleport.other")
    public void teleportOther(@Context CommandCause sender, @Arg("target") ServerPlayer target, @Arg("to") ServerPlayer to) {
        target.setLocation(to.serverLocation());
        sender.audience().sendMessage(Component.text("Player " + target.name() + " was teleported to player " + to.name()));
    }

}
