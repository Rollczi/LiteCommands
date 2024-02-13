package dev.rollczi.example.sponge.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import static net.kyori.adventure.text.Component.text;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

@Command(name = "kick")
@Permission("dev.rollczi.kick")
public class KickCommand {

    @Execute
    public void kickPlayer(@Bind Server server, @Arg("target") ServerPlayer target, @Join("reason") @Key("color") Component reason) {
        target.kick(reason);
        server.sendMessage(text("Player " + target.name() + " has been kicked! (").append(reason).append(text(")")).color(NamedTextColor.RED));
    }

}
