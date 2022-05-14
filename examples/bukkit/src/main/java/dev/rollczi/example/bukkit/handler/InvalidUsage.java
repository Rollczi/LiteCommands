package dev.rollczi.example.bukkit.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.scheme.Scheme;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Scheme scheme) {
        List<String> schemes = scheme.getSchemes();

        if (schemes.size() == 1) {
            sender.sendMessage("&cNie poprawne użycie komendy &8>> &7" + schemes.get(0));
            return;
        }

        sender.sendMessage("&cNie poprawne użycie komendy!");
        for (String sch : schemes) {
            sender.sendMessage("&8 >> &7" + sch);
        }
    }

}
