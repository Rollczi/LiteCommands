package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.network.socket.Server;

import java.util.HashSet;
import java.util.Set;

class LiteMinestomRegistryPlatform implements RegistryPlatform<CommandSender> {

    private final Set<String> commands = new HashSet<>();

    private final CommandManager commandManager;
    private ExecuteResultHandler<CommandSender> executeResultHandler = new ExecuteResultHandler<>();
    private final boolean nativePermissions;

    LiteMinestomRegistryPlatform(Server ignoredServer, boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        this.commandManager = MinecraftServer.getCommandManager();
    }

    @Override
    public void registerListener(CommandSection<CommandSender> command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        SimpleCommand minestomSimpleCommand = this.createCommand(command, executeListener, suggestionListener);

        this.commandManager.register(minestomSimpleCommand);
        this.commands.add(command.getName());
        this.commands.addAll(command.getAliases());
    }

    private SimpleCommand createCommand(CommandSection<CommandSender> command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        if (!this.nativePermissions) {
            return new SimpleCommand(command, executeListener, suggestionListener);
        }

        MinestomNoPermission noPermissionHandler = (sender, requiredPermissions) -> {
            LiteInvocation invocation = new LiteInvocation(new MinestomSender(sender), command.getName(), command.getName());

            try {
                this.executeResultHandler.handleResult(sender, invocation, requiredPermissions);
                return true;
            } catch (IllegalStateException exception) {
                return false;
            }
        };

        return new SimpleBlockedCommand(command, executeListener, suggestionListener, noPermissionHandler);
    }

    @Override
    public void unregisterListener(CommandSection<CommandSender> command) {
        Command commandToDelete = this.commandManager.getCommand(command.getName());

        if (commandToDelete == null) {
            return;
        }

        this.commandManager.unregister(commandToDelete);
        this.commands.remove(command.getName());
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            Command commandToDelete = this.commandManager.getCommand(command);
            if(commandToDelete != null)
                this.commandManager.unregister(commandToDelete);
        }

        this.commands.clear();
    }

    void setExecuteResultHandler(ExecuteResultHandler<CommandSender> executeResultHandler) {
        this.executeResultHandler = executeResultHandler;
    }
}
