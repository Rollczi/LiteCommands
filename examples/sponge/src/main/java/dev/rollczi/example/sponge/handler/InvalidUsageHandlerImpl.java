package dev.rollczi.example.sponge.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.api.command.CommandCause;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandCause> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandCause> invocation, InvalidUsage<CommandCause> result, ResultHandlerChain<CommandCause> chain) {
        Schematic schematic = result.getSchematic();
        CommandCause sender = invocation.sender();

        if (schematic.isOnlyFirst()) {
            sender.audience().sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command<gray> - " + schematic.first()));
            return;
        }

        sender.audience().sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command"));
        for (String rawSchematic : schematic.all()) {
            sender.audience().sendMessage(MINI_MESSAGE.deserialize("<gray> - " + rawSchematic));
        }
    }

}
