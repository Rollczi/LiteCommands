package dev.rollczi.example.bukkit.handler;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invalid.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        List<String> schematics = result.getSchematic().all();

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
