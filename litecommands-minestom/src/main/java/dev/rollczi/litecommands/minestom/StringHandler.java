package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.CommandSender;

class StringHandler implements Handler<CommandSender, String> {

    static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
        .postProcessor(new LegacyProcessor())
        .build();

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        sender.sendMessage(MINI_MESSAGE.deserialize(value));
    }
}
