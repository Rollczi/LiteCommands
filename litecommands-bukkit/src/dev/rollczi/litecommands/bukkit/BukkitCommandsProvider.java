package dev.rollczi.litecommands.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.Map;

public interface BukkitCommandsProvider {

    CommandMap commandMap();

    Map<String, Command> knownCommands();

}
