package dev.rollczi.example.bukkit.command.currency;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;

@Command(name = "/slash")
public class DoubleSlashCommand {

    @Execute
    void executeSlash(@Sender CommandSender sender) {
        sender.sendMessage("Double slash command! Hi " + sender.getName() + "!");
    }

}
