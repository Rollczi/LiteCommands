package dev.rollczi.litecommands.bukkit.context;

import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class LocationContext implements ContextProvider<CommandSender, Location> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public LocationContext(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Location> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;

            return ContextResult.ok(() -> entity.getLocation());
        } else if (sender instanceof BlockCommandSender) {
            BlockCommandSender blockCommandSender = (BlockCommandSender) sender;

            return ContextResult.ok(() -> blockCommandSender.getBlock().getLocation());
        }

        return ContextResult.error(messageRegistry.get(LiteBukkitMessages.LOCATION_NO_CONSOLE, invocation));
    }

}
