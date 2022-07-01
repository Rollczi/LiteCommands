package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.shared.adventure.LegacyProcessor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

class StringHandler implements Handler<CommandSender, String> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyProcessor())
            .build();

    private final KyoriComponentHandler kyoriComponentHandler;

    StringHandler(KyoriComponentHandler kyoriComponentHandler) {
        this.kyoriComponentHandler = kyoriComponentHandler;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        this.kyoriComponentHandler.handle(sender, invocation, MINI_MESSAGE.deserialize(value));
    }

}
