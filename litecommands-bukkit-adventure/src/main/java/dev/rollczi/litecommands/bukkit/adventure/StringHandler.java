package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

class StringHandler implements Handler<CommandSender, String> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyProcessor())
            .build();

    private final KyoriAudienceExtractor kyoriAudienceExtractor;

    StringHandler(KyoriAudienceExtractor kyoriAudienceExtractor) {
        this.kyoriAudienceExtractor = kyoriAudienceExtractor;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        this.kyoriAudienceExtractor.extract(sender).sendMessage(MINI_MESSAGE.deserialize(value));
    }

}
