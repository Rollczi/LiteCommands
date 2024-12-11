package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;

class MinestomPlatform extends AbstractPlatform<CommandSender, LiteMinestomSettings> {

    private final CommandManager commandManager;

    MinestomPlatform(CommandManager commandManager) {
        super(new LiteMinestomSettings(), sender -> new MinestomSender(sender));
        this.commandManager = commandManager;
    }

    @Override
    protected void hook(CommandRoute<CommandSender> command, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        MinestomCommand minestomMinestomCommand = this.createCommand(command, invocationHook, suggestionHook);

        this.commandManager.register(minestomMinestomCommand);
    }

    @Override
    protected void unhook(CommandRoute<CommandSender> command) {
        Command commandToDelete = this.commandManager.getCommand(command.getName());

        if (commandToDelete == null) {
            return;
        }

        this.commandManager.unregister(commandToDelete);
    }

    private MinestomCommand createCommand(CommandRoute<CommandSender> command, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        return new MinestomCommand(command, invocationHook, suggestionHook);
    }

}
