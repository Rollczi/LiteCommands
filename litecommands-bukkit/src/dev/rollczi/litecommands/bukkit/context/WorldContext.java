package dev.rollczi.litecommands.bukkit.context;

import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldContext implements ContextProvider<CommandSender, World> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public WorldContext(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<World> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            return ContextResult.ok(() -> player.getWorld());
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteBukkitMessages.WORLD_PLAYER_ONLY, invocation));
    }

}
