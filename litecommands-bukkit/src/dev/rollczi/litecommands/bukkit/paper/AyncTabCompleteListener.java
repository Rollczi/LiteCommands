package dev.rollczi.litecommands.bukkit.paper;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

public interface AyncTabCompleteListener extends Listener {

    List<String> onTabComplete(CommandSender sender, String alias, String args[]);

}
