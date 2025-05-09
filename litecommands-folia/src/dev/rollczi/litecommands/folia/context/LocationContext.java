package dev.rollczi.litecommands.folia.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.folia.LiteFoliaMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocationContext implements ContextProvider<CommandSender, Location> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public LocationContext(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Location> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            return ContextResult.ok(() -> player.getLocation());
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteFoliaMessages.LOCATION_PLAYER_ONLY, invocation));
    }

}
