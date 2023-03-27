package dev.rollczi.example.velocity.handler;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class InvalidUsage implements InvalidUsageHandler<CommandSource> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(CommandSource commandSource, LiteInvocation invocation, Schematic schematic) {
        List<String> rawSchematics = schematic.getSchematics();

        if (rawSchematics.size() == 1) {
            commandSource.sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command<gray> - " + rawSchematics.get(0)));
            return;
        }

        commandSource.sendMessage(MINI_MESSAGE.deserialize("<red>Incorrect usage of command"));
        for (String rawSchematic : rawSchematics) {
            commandSource.sendMessage(MINI_MESSAGE.deserialize("<gray> - " + rawSchematic));
        }
    }

}
