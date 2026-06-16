package dev.rollczi.example.fabric.server.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

@Command(name = "/slash")
public class DoubleSlashCommand {

    @Execute
    void executeSlash(@Sender CommandSourceStack sender) {
        sender.sendSystemMessage(Component.literal("Double slash command! Hi " + sender.getTextName() + "!"));
    }

}
