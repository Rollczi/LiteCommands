package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.minimessage.MiniMessage;

class StringHandler implements Handler<CommandSource, String> {

    static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyProcessor())
            .build();

    @Override
    public void handle(CommandSource commandSource, LiteInvocation invocation, String value) {
        commandSource.sendMessage(MINI_MESSAGE.deserialize(value));
    }

}
