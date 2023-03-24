package dev.rollczi.litecommands.paper;

import dev.rollczi.litecommands.bukkit.BukkitCommandsProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBukkitCommandProvider implements BukkitCommandsProvider {

    private final CommandMap commandMap;
    private final Map<String, Command> knownCommands;

    public TestBukkitCommandProvider() {
        this.commandMap = mock(CommandMap.class);
        this.knownCommands = new HashMap<>();

        when(commandMap.register(anyString(), anyString(), any())).thenAnswer(invocation -> {
            Command command = invocation.getArgument(2);
            knownCommands.put(command.getName(), command);
            return true;
        });
    }

    @Override
    public CommandMap commandMap() {
        return commandMap;
    }

    @Override
    public Map<String, Command> knownCommands() {
        return knownCommands;
    }

}
