package dev.rollczi.example.fabric.server.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Command(name = "/slash")
public class DoubleSlashCommand {

    @Execute
    void executeSlash(@Sender ServerCommandSource sender) {
        sender.sendMessage(Text.of("Double slash command! Hi " + sender.getName() + "!"));
    }

}
