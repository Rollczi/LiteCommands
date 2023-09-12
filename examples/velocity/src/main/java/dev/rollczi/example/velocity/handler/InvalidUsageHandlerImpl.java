package dev.rollczi.example.velocity.handler;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invalid.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSource> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandSource> invocation, InvalidUsage<CommandSource> result, ResultHandlerChain<CommandSource> chain) {
        Schematic schematic = result.getSchematic();
        CommandSource sender = invocation.sender();

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
