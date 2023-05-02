package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;

class MinestomPlatform extends AbstractPlatform<CommandSender, LiteMinestomSettings> {

    private final CommandManager commandManager;

    MinestomPlatform(CommandManager commandManager) {
        super(new LiteMinestomSettings());
        this.commandManager = commandManager;
    }

    @Override
    protected void hook(CommandRoute<CommandSender> command, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
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

    private MinestomCommand createCommand(CommandRoute<CommandSender> command, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
        return new MinestomCommand(command, invocationHook, suggestionHook);
    }

}
