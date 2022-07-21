package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;

class StringHandler implements Handler<CommandSender, String> {

    private final KyoriAudienceProvider kyoriAudienceProvider;
    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    StringHandler(KyoriAudienceProvider kyoriAudienceProvider, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.kyoriAudienceProvider = kyoriAudienceProvider;
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        this.kyoriAudienceProvider.sender(sender).sendMessage(this.kyoriComponentSerializer.deserialize(value));
    }

}
