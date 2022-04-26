package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.LiteAbstractPlatformManager;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiteBukkitPlatformManager extends LiteAbstractPlatformManager<CommandSender> {

    private final Set<String> commands = new HashSet<>();
    private final Map<String, Command> knownCommands;
    private final CommandMap commandMap;
    private final String fallbackPrefix;

    @SuppressWarnings("unchecked")
    public LiteBukkitPlatformManager(Server server, String fallbackPrefix) {
        super(LiteBukkitSender::new);
        this.fallbackPrefix = fallbackPrefix;

        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(server);

            Field knownCommandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandMapField.setAccessible(true);

            this.knownCommands = (Map<String, Command>) knownCommandMapField.get(this.commandMap);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void registerCommand(ScopeMetaData scope, Executor executor, Suggester suggester) {
        LiteBukkitCommand command = new LiteBukkitCommand(scope, executor, suggester, this.liteSenderCreator);

        this.commandMap.register(scope.getName(), this.fallbackPrefix, command);
        this.commands.add(scope.getName());
        this.commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        for (String command : commands) {
            this.knownCommands.remove(command);
        }
    }

}
