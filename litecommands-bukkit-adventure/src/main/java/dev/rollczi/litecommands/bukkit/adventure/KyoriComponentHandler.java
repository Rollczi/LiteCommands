package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

class KyoriComponentHandler implements Handler<CommandSender, Component> {

    private final KyoriComponentSender kyoriComponentSender;

    KyoriComponentHandler(KyoriComponentSender kyoriComponentSender) {
        this.kyoriComponentSender = kyoriComponentSender;
    }

    @Override
    public void handle(CommandSender commandSource, LiteInvocation invocation, Component value) {
        this.kyoriComponentSender.send(commandSource, value);
    }

}
