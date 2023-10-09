package dev.rollczi.example.bukkit.handler;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class ExampleInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            sender.sendMessage(ChatUtil.color("&cInvalid usage of command! &7(" + schematic.first() + ")"));
            return;
        }

        sender.sendMessage(ChatUtil.color("&cInvalid usage of command!"));
        for (String scheme : schematic.all()) {
            sender.sendMessage(ChatUtil.color("&8 - &7" + scheme));
        }
    }

}
