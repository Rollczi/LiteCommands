package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

class KyoriComponentHandler implements Handler<CommandSender, Component> {

    private final KyoriAudienceExtractor extractor;

    KyoriComponentHandler(KyoriAudienceExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public void handle(CommandSender commandSource, LiteInvocation invocation, Component value) {
        this.extractor.extract(commandSource).sendMessage(value);
    }

}
