package dev.rollczi.example.fabric.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * 2024/2/13<br>
 * LiteCommands<br>
 *
 * @author huanmeng_qwq
 */
@Command(name = "ban")
public class BanCommand {
    @Execute
    void execute(@Arg("player") ServerPlayerEntity player, @Join("reason") String reason) {
        player.networkHandler.disconnect(Text.of(reason));
    }

    @Execute(name = "warnSelf")
    Text execute(@Quoted @Arg String reason) {
        return Text.of("You have been warned: " + reason);
    }
}
