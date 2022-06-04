package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Pattern;

class StringHandler implements Handler<CommandSource, String> {

    // support legacy and new syntax
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(component -> component.replaceText(builder -> builder.match(Pattern.compile(".*")).replacement((matchResult, build) -> SERIALIZER.deserialize(matchResult.group()))))
            .build();

    @Override
    public void handle(CommandSource commandSource, LiteInvocation invocation, String value) {
        commandSource.sendMessage(MINI_MESSAGE.deserialize(value));
    }

}
