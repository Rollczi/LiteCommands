package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

class KyoriComponentHandler implements Handler<CommandSender, Component> {

    private final KyoriAudienceProvider kyoriAudienceProvider;

    KyoriComponentHandler(KyoriAudienceProvider kyoriAudienceProvider) {
        this.kyoriAudienceProvider = kyoriAudienceProvider;
    }

    @Override
    public void handle(CommandSender commandSource, LiteInvocation invocation, Component value) {
        this.kyoriAudienceProvider.sender(commandSource).sendMessage(value);
    }

}
