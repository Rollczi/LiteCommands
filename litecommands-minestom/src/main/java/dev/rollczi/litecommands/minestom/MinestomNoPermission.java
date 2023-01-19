package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import net.minestom.server.command.CommandSender;

import java.util.function.BiFunction;

interface MinestomNoPermission extends BiFunction<CommandSender, RequiredPermissions, Boolean> {

}
