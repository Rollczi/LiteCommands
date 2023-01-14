package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import org.bukkit.command.CommandSender;

import java.util.function.BiFunction;

interface BukkitNoPermission extends BiFunction<CommandSender, RequiredPermissions, Boolean> {

}
