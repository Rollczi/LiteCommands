package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.command.CommandSender;

class StringHandler implements Handler<CommandSender, String> {

    static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
        .character('&')
        .useUnusualXRepeatedCharacterHexFormat()
        .hexColors()
        .build();

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        sender.sendMessage(SERIALIZER.deserialize(value));
    }

}
