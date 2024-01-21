package dev.rollczi.example.minestom.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.CommandSender;

public class MyInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandSender> invocation, dev.rollczi.litecommands.invalidusage.InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();
        CommandSender sender = invocation.sender();

        if (schematic.isOnlyFirst()) {
            sender.sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command<gray> - " + schematic.first()));
            return;
        }

        sender.sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command"));
        for (String rawSchematic : schematic.all()) {
            sender.sendMessage(MINI_MESSAGE.deserialize("<gray> - " + rawSchematic));
        }
    }

}
