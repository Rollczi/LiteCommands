package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.component.ScopeMetaData;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiteBukkitCommandManager implements LiteCommandManager {

    private final Set<String> commands = new HashSet<>();
    private final CommandMap commandMap;
    private final Map<String, Command> knownCommands;
    private final String fallbackPrefix;

    @SuppressWarnings("unchecked")
    public LiteBukkitCommandManager(Server server, String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;

        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(server);

            Field knownCommandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandMapField.setAccessible(true);

            knownCommands = (Map<String, Command>) knownCommandMapField.get(commandMap);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void registerCommand(ScopeMetaData scope, Executor executor, Suggester suggester) {
        LiteBukkitCommand command = new LiteBukkitCommand(scope, executor, suggester);

        commandMap.register(scope.getName(), fallbackPrefix, command);
        commands.add(scope.getName());
        commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        for (String command : commands) {
            knownCommands.remove(command);
        }
    }

}
