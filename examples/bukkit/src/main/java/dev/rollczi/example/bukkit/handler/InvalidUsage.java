package dev.rollczi.example.bukkit.handler;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InvalidUsage implements CommandExecuteResultHandler<CommandSender> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        List<String> schematics = schematic.getSchematics();

        if (schematics.size() == 1) {
            sender.sendMessage(ChatUtil.color("&cNie poprawne użycie komendy &8>> &7" + schematics.get(0)));
            return;
        }

        sender.sendMessage(ChatUtil.color("&cNie poprawne użycie komendy!"));
        for (String sch : schematics) {
            sender.sendMessage(ChatUtil.color("&8 >> &7" + sch));
        }
    }

}
