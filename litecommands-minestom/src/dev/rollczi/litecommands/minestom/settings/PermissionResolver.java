package dev.rollczi.litecommands.minestom.settings;

import net.minestom.server.command.CommandSender;

@FunctionalInterface
public interface PermissionResolver {

    boolean hasPermission(CommandSender sender, String permission);
}
